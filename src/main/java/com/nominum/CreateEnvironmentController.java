package com.nominum;


import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.ws.rs.core.MediaType;
import java.io.IOException;

/**
 * Created by vpathi on 12/20/16.
 */
@Controller
public class CreateEnvironmentController {

    private Executor executor;
    private ConsoleVmOutput consoleVmOutput;
    private org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());

    public CreateEnvironmentController() {

        executor = new Executor();


    }

    @GetMapping("/environment")
    public String environmentForm(Model model) {
        model.addAttribute("environment", new Environment());
        return "environment";
    }


    @PostMapping(value = "/environment", produces = MediaType.TEXT_PLAIN)
    public StreamingResponseBody environmentSubmit(@ModelAttribute Environment environment) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        Configuration configuration = Configuration.fromPostParams(
                environment.getDriver(),
                environment.getVersion(),
                currentUserName);
        return executor.execute(configuration);
    }

    @GetMapping(value = "/consolelogs")
    public ResponseEntity<StreamingResponseBody> consoleLogs() throws IOException {
        consoleVmOutput = new ConsoleVmOutput();
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new org.springframework.http.MediaType("text", "event-stream"));
        return new ResponseEntity(consoleVmOutput.getLogs(), headers, HttpStatus.OK);
    }

    @PostMapping(value = "/delete/environment",produces = MediaType.TEXT_PLAIN)
    public StreamingResponseBody environmetSubmit(@ModelAttribute VmTableForm vmTableForm){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        currentUserName = authentication.getName();
        Configuration configuration = Configuration.deleteFromPostParams(
                vmTableForm.getVmName(), currentUserName);

        return executor.execute(configuration);

    }
}
