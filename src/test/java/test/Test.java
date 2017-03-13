package test;

import com.proxy.util.ThreadUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by zhoukai on 2016/12/2.
 */
public class Test {
    public static void main(String[] args){
        List<Callable<String>> tasks = new ArrayList<Callable<String>>();
        for(int i =0 ;i<500;i++){
            tasks.add(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    try {
                        for(int j=0;j<2;j++){
                            int index = ThreadLocalRandom.current().nextInt(500);
                            if(j==1){
                                System.out.println(index);
                            }
                        }

                    } catch (Exception e) {
                        //TODO 不做操作
                    }
                    return null;
                }
            });
        }
        try {
            ThreadUtils.EXECUTOR.invokeAll(tasks);
        } catch (InterruptedException e) {
           e.printStackTrace();
        }
    }
}
