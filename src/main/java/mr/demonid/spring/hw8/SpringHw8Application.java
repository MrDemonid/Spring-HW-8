package mr.demonid.spring.hw8;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringHw8Application {

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        SpringApplication.run(SpringHw8Application.class, args);
        long runTime = System.currentTimeMillis() - startTime;
        System.out.printf("Start at %d сек %d ms\n", runTime/1000, runTime % 1000);
    }

}
