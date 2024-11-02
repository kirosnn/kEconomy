package fr.kirosnn.keconomy.api;

import fr.kirosnn.keconomy.config.MainConfig;
import fr.kirosnn.keconomy.kEconomy;
import io.github.projectunified.minelib.scheduler.async.AsyncScheduler;
import io.github.projectunified.minelib.scheduler.common.task.Task;
import io.github.projectunified.minelib.scheduler.global.GlobalScheduler;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AutoSaveEconomyHandler extends EconomyHandler implements Runnable {
    private final AtomicBoolean needSaving = new AtomicBoolean();
    private Task task;

    protected AutoSaveEconomyHandler(kEconomy instance) {
        super(instance);
        int period = instance.get(MainConfig.class).getSaveFilePeriod();
        if (period >= 0) {
            task = AsyncScheduler.get(instance).runTimer(
                    this,
                    instance.get(MainConfig.class).getSaveFilePeriod(),
                    instance.get(MainConfig.class).getSaveFilePeriod()
            );
        }
    }

    @Override
    public final void run() {
        if (!needSaving.get()) {
            return;
        }
        GlobalScheduler.get(instance).run(() -> {
            this.save();
            needSaving.set(false);
        });
    }

    protected abstract void save();

    protected void enableSave() {
        needSaving.lazySet(true);
    }

    @Override
    public void disable() {
        if (task != null) {
            task.cancel();
        }
        save();
    }
}
