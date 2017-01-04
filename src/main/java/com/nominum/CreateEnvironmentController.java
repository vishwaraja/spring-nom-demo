package com.nominum;


import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.ws.rs.core.MediaType;

/**
 * Created by vpathi on 12/20/16.
 */
@Controller
public class CreateEnvironmentController {

    private Executor executor;
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
                environment.getVmName(),
                currentUserName);
        StreamingResponseBody response = executor.execute(configuration);
        return response;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN)
    public StreamingResponseBody createStack(@RequestParam(value = "driver-type") String driver,
                                             @RequestParam(value = "version") String version,
                                             @RequestParam(value = "vmname") String vmname,
                                             @RequestParam(value = "username") String userName) {

        Configuration configuration = Configuration.fromPostParams(driver, version, vmname, userName);
        return executor.execute(configuration);
    }


}
