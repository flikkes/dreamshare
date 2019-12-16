package de.hszg.luepke.dreamshare.rendering;

import java.io.StringWriter;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("renderer")
public class RendererRestController {

    @GetMapping("script")
    public ResponseEntity<?> getRenderer(@RequestParam String rendererName) {
		Properties p = new Properties();
		p.setProperty("resource.loader", "class");
		p.setProperty("class.resource.loader.class",
				"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		Velocity.init(p);
		VelocityContext context = new VelocityContext();

		Template t = Velocity.getTemplate("templates/defaultRenderer.js.vm");

        context.put("baseUrl", "http://localhost:8080/content/image");

        StringWriter writer = new StringWriter();
        t.merge(context, writer);
        return ResponseEntity.ok(writer.toString());
    }
}