import java.util.Arrays;
import java.util.List;

public class Application {
    private static final String WORKER_ADDRESS_1 = "http://localhost:8081/task";
    private static final String WORKER_ADDRESS_2 = "http://localhost:8082/task";

    public static void main(String []args){
        Aggregator aggregator = new Aggregator();
        String task1 = "10, 200";
        String task2 = "123456789, 1000000000000000, 700000002342343";

        List<String> results = aggregator.sendTaskToWorkers(Arrays.asList(WORKER_ADDRESS_1, WORKER_ADDRESS_2),
                Arrays.asList(task1, task2));

        for(String result: results){
            System.out.println(result);
        }
    }
}
