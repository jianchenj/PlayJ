#ifndef PLAYERJ_JAVACALLHELPER_H
#define PLAYERJ_JAVACALLHELPER_H

#include <jni.h>

class JavaCallHelper {
public:
    JavaCallHelper(JavaVM * _javaVM , JNIEnv* _env , jobject &_obj);
    ~JavaCallHelper();

    void onPrepare(int thread);

    void onProgress(int thread, int progress);

    void onError(int thread , int code);
private:
    JavaVM* javaVm;
    JNIEnv* env;
    jobject jobj;
    jmethodID jmid_prepare;
    jmethodID jmid_error;
    jmethodID jmid_progress;
};


#endif //PLAYERJ_JAVACALLHELPER_H
