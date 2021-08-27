package com.br.integra;

import java.io.IOException;
import java.net.URISyntaxException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.br.integra.Rcaller.RUtils;
import com.github.rcaller.rstuff.RCaller;
import com.github.rcaller.rstuff.RCallerOptions;
import com.github.rcaller.rstuff.RCode;

@SpringBootApplication
public class IntegraAnomaliaApplication {

	public static void main(String[] args) {
		SpringApplication.run(IntegraAnomaliaApplication.class, args);
		
		int values[] = {10,20,30,10,15,24,55};
		try {
			System.out.println(mean(values)[0]);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
    public static int[] mean(int[] values) throws IOException, URISyntaxException {
        
    	String fileContent = RUtils.getScriptContent();
        RCode code = RCode.create();
        code.addRCode(fileContent);
        code.addInt("input", values[0]);
        code.addRCode("result <- customMean(input)");
        RCaller caller = RCaller.create(code, RCallerOptions.create());
        caller.runAndReturnResult("result");
        return caller.getParser()
            .getAsIntArray("result");
    }

}
