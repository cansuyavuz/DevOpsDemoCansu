package ch.zhaw.iwi.devops.demo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class HelpdeskController {

    private Map<Integer, Helpdesk> help = new HashMap<>();

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        this.help.put( 1, new help(1, "Computerproblem", "Bluescreen beim Hochfahren"));
        this.help.put( 2, new Helpdesk(2, "Netzwerkproblem", "Langsame Internetverbindung"));
        this.help.put( 3, new Helpdesk(3, "Druckerproblem", "Drucker druckt nicht"));
        System.out.println("Init Data");
    }

    @GetMapping("/services/helpdesk")
    public List<PathListEntry<Integer>> getHelpdesks() {
        var result = new ArrayList<PathListEntry<Integer>>();
        for (var helpdesk : this.helpdesks.values()) {
            var entry = new PathListEntry<Integer>();
            entry.setKey(helpdesk.getId(), "helpdeskKey");
            entry.setName(helpdesk.getTitle());
            entry.getDetails().add(helpdesk.getDescription());
            entry.setTooltip(helpdesk.getDescription());
            result.add(entry);
        }
        return result;
    }

    @GetMapping("/services/helpdesk/{id}")
    public Helpdesk getHelpdesk(@PathVariable Integer id) {
        return this.helpdesks.get(id);
    }

    @PostMapping("/services/helpdesk")
    public void createHelpdesk(@RequestBody Helpdesk helpdesk) {
        var newId = this.helpdesks.keySet().stream().max(Comparator.naturalOrder()).orElse(0) + 1;
        helpdesk.setId(newId);
        this.helpdesks.put(newId, helpdesk);
    }

    @PutMapping("/services/helpdesk/{id}")
    public void updateHelpdesk(@PathVariable Integer id, @RequestBody Helpdesk helpdesk) {
        helpdesk.setId(id);
        this.helpdesks.put(id, helpdesk);
    }

    @DeleteMapping("/services/helpdesk/{id}")
    public Helpdesk deleteHelpdesk(@PathVariable Integer id) {
        return this.helpdesks.remove(id);
    }
}
