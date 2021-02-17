import networking.WebClient;

import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Aggregator {
    private WebClient webClient;

    public Aggregator(){
        this.webClient = new WebClient();
    }

    public List<String> sendTaskToWorkers(List<String> workerAddresses, List<String> tasks){
        CompletableFuture<String>[] futures = new CompletableFuture[workerAddresses.size()];

        for(int i = 0; i< workerAddresses.size();i++){
            String workerAddress = workerAddresses.get(i);
            String task = tasks.get(i);

            byte[] requestPayload = task.getBytes();

            futures[i] = webClient.sendTask(workerAddress,requestPayload);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        /*
        List<String> results = new ArrayList<>();
        for(int i = 0; i<tasks.size(); i++){
            results.add(futures[i].join());
        }
        code below is equivalent
         */
        List<String> results = Stream.of(futures).map(CompletableFuture::join)
                                .collect(Collectors.toList());
        return results;
    }
}
