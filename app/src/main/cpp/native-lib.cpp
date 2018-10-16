#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_group2_pacepal_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Pace Pal!";
    return env->NewStringUTF(hello.c_str());
}
