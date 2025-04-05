package bap.jp.mvcbap.batch.bonus;

import org.springframework.stereotype.Component;

@Component
public class BatchSchedulerControl {
    private volatile boolean exportJobEnabled = true;
    private volatile boolean importJobEnabled = true;
    private volatile boolean cleanupJobEnabled = true;

    public boolean isExportJobEnabled() {
	return exportJobEnabled;
    }

    public void setExportJobEnabled(boolean exportJobEnabled) {
	this.exportJobEnabled = exportJobEnabled;
    }

    public boolean isImportJobEnabled() {
	return importJobEnabled;
    }

    public void setImportJobEnabled(boolean importJobEnabled) {
	this.importJobEnabled = importJobEnabled;
    }

    public boolean isCleanupJobEnabled() {
	return cleanupJobEnabled;
    }

    public void setCleanupJobEnabled(boolean cleanupJobEnabled) {
	this.cleanupJobEnabled = cleanupJobEnabled;
    }
}
