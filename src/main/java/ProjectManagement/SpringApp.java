package ProjectManagement;

import ProjectManagement.controllers.BoardController;
import ProjectManagement.services.TaskService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class SpringApp extends SpringBootServletInitializer {
    public int[] twoSum(int[] numbers, int target) {
        Map<Integer,Integer> hashTable=new HashMap<>();
        for(int i=0;i<numbers.length;i++){
            int complement=target-numbers[i];
            if(hashTable.containsKey(numbers[i])){
                return new int[]{hashTable.get(complement),i+1};
            }
            if(!hashTable.containsKey(target-numbers[i]))hashTable.put(target-numbers[i],i+1);
        }
        return null;
    }
    public static void main(String[] args) {

        SpringApplication.run(SpringApp.class, args);
    }
}
