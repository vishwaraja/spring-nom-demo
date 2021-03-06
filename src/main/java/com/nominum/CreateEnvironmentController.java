package com.nominum;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.thymeleaf.util.StringUtils;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by vpathi on 12/20/16.
 */
@Controller
public class CreateEnvironmentController {

    private Executor executor;
    private ConsoleVmOutput consoleVmOutput;


    public CreateEnvironmentController() {
        executor = new Executor();
    }

    @GetMapping("/environment")
    public String environmentForm(Model model) {
        model.addAttribute("environment", new Environment());
        return "environment";
    }

    @GetMapping("/list")
    public String list() {
        return "list";
    }

    @PostMapping(value = "/environment", produces = MediaType.TEXT_PLAIN)
    public ResponseEntity<String> environmentSubmit(@ModelAttribute Environment environment) {
        Configuration configuration = Configuration.fromPostParams(
                environment.getDriver(),
                environment.getVersion(),
                getUserName());
        Thread t1 =new Thread(new Runnable() {
            @Override
            public void run() {
                executor.execute(configuration);
            }
        });
        t1.start();

        return ResponseEntity.ok("");
    }

    @GetMapping(value = "/console/logs")
    public ResponseEntity<StreamingResponseBody> consoleLogs() throws IOException {
        consoleVmOutput = new ConsoleVmOutput();
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new org.springframework.http.MediaType("text", "event-stream"));
        return new ResponseEntity(consoleVmOutput.getLogs(), headers, HttpStatus.OK);
    }

    @GetMapping(value = "/machine/{vmName}/{action}",produces = MediaType.TEXT_PLAIN)
    public ResponseEntity<String> deleteEnvironment(@PathVariable("vmName") String vmName,@PathVariable String action, Model model){
        Configuration configuration = Configuration.forVmActions(
                vmName, getUserName(), action);
        executor.execute(configuration);
        return ResponseEntity.ok("");

    }

    @ModelAttribute("vmInfo")
    public List<VmInfo> getDockerMachineInfo() {
        return executor.executeAsStringOutput(Configuration.forVmList(getUserName()))
                .stream()
                .filter(vm-> !StringUtils.isEmpty(vm))
                .map(vm -> {
                    Configuration vmConfig = Configuration.forVmListInfo(getUserName(), vm);
                    List<String> statusOutput = executor.executeAsStringOutput(vmConfig);

                    VmInfo vmInfo = new VmInfo();
                    vmInfo.setVmName(statusOutput.get(3));
                    vmInfo.setUrl(statusOutput.get(0));
                    vmInfo.setStatus(statusOutput.get(1));
                    vmInfo.setDriver(statusOutput.get(2));

                    return vmInfo;

                }).collect(Collectors.toList());
    }

    @ModelAttribute("username")
    public String getUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }


}

