package de.hszg.luepke.dreamshare.rendering;

import java.io.StringWriter;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.apache.velocity.runtime.resource.loader.FileResourceLoader;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.hszg.luepke.dreamshare.content.ImageEntity;

@RestController
@RequestMapping("renderer")
public class RendererRestController {

    @GetMapping("")
    public ResponseEntity<?> getRenderer(@RequestParam String rendererName) {
        VelocityEngine ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        ve.init();

        Template t = ve.getTemplate("templates/defaultRenderer.js.vm");

        VelocityContext context = new VelocityContext();
        context.put("entityClass", ImageEntity.class);

        StringWriter writer = new StringWriter();
        t.merge(context, writer);
        return ResponseEntity.ok(writer.toString());
    }
}