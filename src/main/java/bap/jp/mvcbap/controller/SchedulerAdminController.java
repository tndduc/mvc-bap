package bap.jp.mvcbap.controller;

import bap.jp.mvcbap.batch.bonus.BatchSchedulerControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/scheduler")
public class SchedulerAdminController {

    private final BatchSchedulerControl schedulerControl;

    @Autowired
    public SchedulerAdminController(BatchSchedulerControl schedulerControl) {
	this.schedulerControl = schedulerControl;
    }

    @GetMapping
    public String showSchedulerStatus(Model model) {
	model.addAttribute("schedulerControl", schedulerControl);
	return "schedulerAdmin";
    }

    @PostMapping("/update")
    public String updateScheduler(@RequestParam(name = "exportJobEnabled", defaultValue = "false") boolean exportJobEnabled,
				  @RequestParam(name = "importJobEnabled", defaultValue = "false") boolean importJobEnabled,
				  @RequestParam(name = "cleanupJobEnabled", defaultValue = "false") boolean cleanupJobEnabled,
				  RedirectAttributes redirectAttributes) {
	schedulerControl.setExportJobEnabled(exportJobEnabled);
	schedulerControl.setImportJobEnabled(importJobEnabled);
	schedulerControl.setCleanupJobEnabled(cleanupJobEnabled);
	redirectAttributes.addFlashAttribute("message", "Cập nhật trạng thái scheduler thành công!");
	return "redirect:/admin/scheduler";
    }
}
