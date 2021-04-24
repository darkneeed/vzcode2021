package com.utopia.vzcodeauth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.beautycoder.pflockscreen.PFFLockScreenConfiguration;
import com.beautycoder.pflockscreen.fragments.PFLockScreenFragment;
import com.beautycoder.pflockscreen.viewmodels.PFPinCodeViewModel;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showLockScreenFragment();
    }

    private final PFLockScreenFragment.OnPFLockScreenCodeCreateListener mCodeCreateListener =
            new PFLockScreenFragment.OnPFLockScreenCodeCreateListener() {
                @Override
                public void onCodeCreated(String encodedCode) {
                    Toast.makeText(MainActivity.this, "Пин-код создан", Toast.LENGTH_SHORT).show();
                    PreferencesSettings.saveToPref(MainActivity.this, encodedCode);
                    showMainFragment();
                }

                @Override
                public void onNewCodeValidationFailed() {
                    Toast.makeText(MainActivity.this, "Ошибка при создании пин-кода", Toast.LENGTH_SHORT).show();
                }
            };

    private final PFLockScreenFragment.OnPFLockScreenLoginListener mLoginListener =
            new PFLockScreenFragment.OnPFLockScreenLoginListener() {

                @Override
                public void onCodeInputSuccessful() {
                    showMainFragment();
                }

                @Override
                public void onFingerprintSuccessful() {
                    showMainFragment();
                }

                @Override
                public void onPinLoginFailed() {
                    Toast.makeText(MainActivity.this, "Не верный пин-код", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFingerprintLoginFailed() {
                    Toast.makeText(MainActivity.this, "Отпечаток не найден", Toast.LENGTH_SHORT).show();
                }
            };

    private void showLockScreenFragment() {
        new PFPinCodeViewModel().isPinCodeEncryptionKeyExist().observe(
                this,
                result -> {
                    if (result == null) {
                        return;
                    }
                    if (result.getError() != null) {
                        return;
                    }
                    showLockScreenFragment(result.getResult());
                }
        );
    }

    private void showLockScreenFragment(boolean isPinExist) {
        PFFLockScreenConfiguration.Builder builder = new PFFLockScreenConfiguration.Builder(this)
                .setTitle(isPinExist ? "" : "Придумайте пин-код")
                .setCodeLength(4)
                .setLeftButton("Забыли пин-код?")
                .setNewCodeValidation(true)
                .setNewCodeValidationTitle("Повторите пин-код")
                .setUseFingerprint(true);
        final PFLockScreenFragment fragment = new PFLockScreenFragment();


        fragment.setOnLeftButtonClickListener(v -> Toast.makeText(MainActivity.this, "Восстановление пин-кода", Toast.LENGTH_LONG).show());

        builder.setMode(isPinExist ? PFFLockScreenConfiguration.MODE_AUTH : PFFLockScreenConfiguration.MODE_CREATE);
        if (isPinExist) {
            fragment.setEncodedPinCode(PreferencesSettings.getCode(this));
            fragment.setLoginListener(mLoginListener);
        }

        fragment.setConfiguration(builder.build());
        fragment.setCodeCreateListener(mCodeCreateListener);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_view, fragment).commit();

    }

    private void showMainFragment() {
        MainFragment fragment = new MainFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_view, fragment).commit();
    }


}
