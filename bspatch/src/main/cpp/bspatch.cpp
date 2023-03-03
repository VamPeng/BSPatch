#include <jni.h>
#include <string>

extern "C" {
extern int bspatch_main(int argc, char *args[]);
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_bspatch_NativeLib_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_bsdiff_PatchUtil_patchApk(JNIEnv *env, jobject thiz, jstring old_apk,
                                           jstring new_apk, jstring patch) {
    const char *pold = env->GetStringUTFChars(old_apk, 0);
    const char *pnew = env->GetStringUTFChars(new_apk, 0);
    const char *ppatch = env->GetStringUTFChars(patch, 0);

    char *argv[] = {
            "bspatch",
            const_cast<char *>(pold),
            const_cast<char *>(pnew),
            const_cast<char *>(ppatch)
    };
    bspatch_main(4, argv);

    env->ReleaseStringUTFChars(old_apk, pold);
    env->ReleaseStringUTFChars(new_apk, pnew);
    env->ReleaseStringUTFChars(patch, ppatch);
}