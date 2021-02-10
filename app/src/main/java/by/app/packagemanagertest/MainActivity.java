package by.app.packagemanagertest;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static android.text.format.Formatter.formatFileSize;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
      //  final TextView nameApp = (TextView) findViewById(R.id.text);
       // final TextView hashApp = (TextView) findViewById(R.id.hash);

        List<AppInfo> appName = getApps();
        for(int i = 0; i < appName.size(); i ++){
            String appHash = "";
            String appText = "";

            appText = appName.get(i).getApplicationLabelem();
            try {
                appHash = getSignatureHash(getApplicationContext(), appName.get(i).getPackageName());
            } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

           // nameApp.setText(appText);
          //  hashApp.setText(appHash);
        }
    }

    private List<AppInfo> getApps() {
        Context context = getApplicationContext();
        final PackageManager pm = context.getPackageManager();
        List<AppInfo> apps = new ArrayList<>();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo appInfo : packages) {
            PackageInfo packageInfo;
            try {
                packageInfo = pm.getPackageInfo(appInfo.packageName, 0);
                File file = new File(appInfo.publicSourceDir);
                String size = formatFileSize(context, file.length());

                AppInfo newApp =
                        new AppInfo(applicationLabel(context, appInfo), appInfo.packageName, appInfo.sourceDir,
                                appInfo.publicSourceDir, packageInfo.versionName, packageInfo.versionCode,
                                isSystemPackage(packageInfo), size, file.length(), appInfo.dataDir,
                                appInfo.nativeLibraryDir, file.lastModified(), packageInfo.firstInstallTime,
                                packageInfo.lastUpdateTime, appInfo.enabled);
                apps.add(newApp);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return apps;
    }



    private String applicationLabel(Context con, ApplicationInfo packageInfo) {
        PackageManager p = con.getPackageManager();
        return p.getApplicationLabel(packageInfo).toString();
    }

    private boolean isSystemPackage(PackageInfo pkgInfo) {
        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }

    public static String getSignatureHash(Context ctxt, String packageName)
            throws PackageManager.NameNotFoundException, NoSuchAlgorithmException {
        MessageDigest md=MessageDigest.getInstance("SHA-256");
        Signature sig=
                ctxt.getPackageManager()
                        .getPackageInfo(packageName, PackageManager.GET_SIGNATURES).signatures[0];

        return(toHexStringWithColons(md.digest(sig.toByteArray())));
    }

    // based on https://stackoverflow.com/a/2197650/115145

    public static String toHexStringWithColons(byte[] bytes) {
        char[] hexArray=
                { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B',
                        'C', 'D', 'E', 'F' };
        char[] hexChars=new char[(bytes.length * 3) - 1];
        int v;

        for (int j=0; j < bytes.length; j++) {
            v=bytes[j] & 0xFF;
            hexChars[j * 3]=hexArray[v / 16];
            hexChars[j * 3 + 1]=hexArray[v % 16];

           // if (j < bytes.length - 1) {
          //      hexChars[j * 3 + 2]=':';
          //  }
        }
        return new String(hexChars);
    }
}