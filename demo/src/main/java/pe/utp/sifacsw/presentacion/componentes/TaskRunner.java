package pe.utp.sifacsw.presentacion.componentes;

import java.util.function.*;

import javafx.concurrent.Task;

public class TaskRunner {

    public static <T> void runWithLoading(Supplier<T> task, Consumer<T> onSuccess) {

        LoadingStage loading = new LoadingStage();
        loading.show();

        Task<T> backgroundTask = new Task<>() {
            @Override
            protected T call() throws Exception {
                return task.get(); // ejecuta la consulta
            }
        };

        backgroundTask.setOnSucceeded(e -> {
            loading.close();
            onSuccess.accept(backgroundTask.getValue());
        });

        backgroundTask.setOnFailed(e -> {
            loading.close();
            e.getSource().getException().printStackTrace();
        });

        new Thread(backgroundTask).start();
    }
}


