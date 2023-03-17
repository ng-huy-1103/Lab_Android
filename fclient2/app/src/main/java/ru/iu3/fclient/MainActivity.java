package ru.iu3.fclient;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import ru.iu3.fclient.databinding.ActivityMainBinding;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class MainActivity extends AppCompatActivity {

    ActivityResultLauncher activityResultLauncher;

    // Used to load the 'fclient' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        int res = initRng();
        byte[] v = randomBytes(10);

        byte[] data = randomBytes(10);
        byte[] key = randomBytes(16);
        byte[] encryptedData = encrypt(key,data);
        byte[] decryptedData = decrypt(key,encryptedData);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        activityResultLauncher  = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult> () {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            // обработка результата
                            String pin = data.getStringExtra("pin");
                            Toast.makeText(MainActivity.this, pin, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    /**
     * A native method that is implemented by the 'fclient' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
    public static native int initRng();
    public static native byte[] randomBytes(int no);
//    public static native byte[] encrypt(byte[] key, byte[] data);
//    public static native byte[] decrypt(byte[] key, byte[] data);

    public static byte[] stringToHex(String s)
    {
        byte[] hex;
        try
        {
            hex = Hex.decodeHex(s.toCharArray());
        }
        catch (DecoderException ex)
        {
            hex = null;
        }
        return hex;
    }

//    public void onButtonClick(View v)
//    {
//        Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
//    }
//    public void onButtonClick(View v)
//    {
//        byte[] key = stringToHex("0123456789ABCDEF0123456789ABCDE0");
//        byte[] enc = encrypt(key, stringToHex("000000000000000102"));
//        byte[] dec = decrypt(key, enc);
//        String s = new String(Hex.encodeHex(dec)).toUpperCase();
//        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
//    }

    public static native byte[] encrypt(byte[] key, byte[] data);
    public static native byte[] decrypt(byte[] key, byte[] data);
    public void onButtonClick(View v)
    {
        byte[] key = stringToHex("0123456789ABCDEF0123456789ABCDE0");
        byte[] enc = encrypt(key, stringToHex("000000000000000102"));
        byte[] dec = decrypt(key, enc);
       String s = new String(Hex.encodeHex(dec)).toUpperCase();

        Intent it = new Intent(this, PinpadActivity.class);
        startActivity(it);
    }



}
