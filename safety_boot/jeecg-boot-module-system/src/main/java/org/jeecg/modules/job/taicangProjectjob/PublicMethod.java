package org.jeecg.modules.job.taicangProjectjob;

import java.io.Closeable;
import java.io.IOException;

public class PublicMethod {
    public PublicMethod() {

    }

    public static void closeAll(Closeable... hh) {

        for (Closeable closeable : hh) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
