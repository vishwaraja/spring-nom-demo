package com.nominum;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

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


    @PostMapping(value = "/environment", produces = MediaType.TEXT_PLAIN)
    public StreamingResponseBody environmentSubmit(@ModelAttribute Environment environment) {
        Configuration configuration = Configuration.fromPostParams(
                environment.getDriver(),
                environment.getVersion(),
                getUserName());
        return executor.execute(configuration);
    }

    @GetMapping(value = "/console/logs")
    public ResponseEntity<StreamingResponseBody> consoleLogs() throws IOException {
        consoleVmOutput = new ConsoleVmOutput();
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new org.springframework.http.MediaType("text", "event-stream"));
        return new ResponseEntity(consoleVmOutput.getLogs(), headers, HttpStatus.OK);
    }

    @PostMapping(value = "/delete/environment",produces = MediaType.TEXT_PLAIN)
    public StreamingResponseBody environmetSubmit(@ModelAttribute VmTableForm vmTableForm){
        Configuration configuration = Configuration.deleteFromPostParams(
                vmTableForm.getVmName(), getUserName());
        return executor.execute(configuration);

    }


    @GetMapping(value = "/vms/info", produces = MediaType.TEXT_PLAIN)
    public ModelAndView getDockerMachineInfo() {

        Configuration configuration = Configuration.forVmList(getUserName());
        List<String> vms = executor.executeAsStringOutput(configuration);

        ModelAndView mv = new ModelAndView("list");
        for (String vm:vms){
            Configuration vmConfig = Configuration.forVmListInfo(getUserName(),vm);
            List<String> statusOutput = executor.executeAsStringOutput(vmConfig);

            VmTableForm vmTableForm = new VmTableForm();
            vmTableForm.setUrl(statusOutput.get(0));
            vmTableForm.setStatus(statusOutput.get(1));
            vmTableForm.setDriver(statusOutput.get(2));
            mv.addObject("vmTableForm", vmTableForm);

            }
        return mv;
        }

    private String getUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
