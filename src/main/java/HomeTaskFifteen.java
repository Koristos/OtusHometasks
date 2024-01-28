import akka.actor.ActorSystem;
import akka.stream.*;
import akka.stream.javadsl.*;
import akka.stream.javadsl.Broadcast;
import akka.stream.javadsl.RunnableGraph;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class HomeTaskFifteen {

    public static void main(String[] args) {

        ActorSystem system = ActorSystem.create("fusion");
        ActorMaterializerSettings settings = ActorMaterializerSettings.create(system);
        ActorMaterializer materializer = ActorMaterializer.create(settings, system, "fusion");


        Graph graph = GraphDSL.create(builder -> {
            SourceShape<Integer> input = builder.add(Source.from(IntStream.range(1, 100).boxed().collect(Collectors.toList())));
            FlowShape mulTen = builder.add(akka.stream.javadsl.Flow.of(Integer.class).map(x -> x * 10));
            FlowShape mulTwo = builder.add(akka.stream.javadsl.Flow.of(Integer.class).map(x -> x * 2));
            FlowShape mulThree = builder.add(akka.stream.javadsl.Flow.of(Integer.class).map(x -> x * 3));

            FlowShape unite = builder.add(Flow.of(Integer.class).grouped(3).map(butch -> butch.stream().mapToInt(a -> a).sum()));
            SinkShape output = builder.add(Sink.foreach(System.out::println));
            UniformFanInShape<Integer, Integer> merge = builder.add(Merge.create(3));

            UniformFanOutShape<Integer, Integer> broadcast = builder.add(Broadcast.create(3));
            builder.from(input)
                    .viaFanOut(broadcast)
                    .via(mulTen)
                    .viaFanIn(merge)
                    .via(unite)
                    .to(output);
            builder.from(broadcast)
                    .via(mulTwo)
                    .toFanIn(merge);
            builder.from(broadcast)
                    .via(mulThree)
                    .toFanIn(merge);

            return ClosedShape.getInstance();
        });
        RunnableGraph.fromGraph(graph).run(materializer);
    }
}
