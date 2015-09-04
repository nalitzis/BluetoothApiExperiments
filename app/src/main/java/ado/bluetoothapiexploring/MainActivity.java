package ado.bluetoothapiexploring;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    private static final String TAG = "MainActivity";

    private TableLayout mRoot;
    private TextView mA2dpConnected;
    private BluetoothProfile.ServiceListener mA2dpServiceListener;
    private BluetoothA2dp mA2dpProfile;

    private TextView mHeadsetConnected;
    private BluetoothProfile.ServiceListener mHeadsetServiceListener;
    private BluetoothHeadset mHeadsetProfile;


    private class A2DPServiceListener implements BluetoothProfile.ServiceListener {

        @Override
        public void onServiceConnected(int profile, BluetoothProfile proxy) {
            Log.d(TAG, "profile a2dp connected");
            mA2dpProfile = (BluetoothA2dp) proxy;
            mA2dpConnected.setText("true");
        }

        @Override
        public void onServiceDisconnected(int profile) {
            mA2dpConnected.setText("false");
        }
    }

    private class HeadsetServiceListener implements BluetoothProfile.ServiceListener {

        @Override
        public void onServiceConnected(int profile, BluetoothProfile proxy) {
            Log.d(TAG, "profile headset connected");
            mHeadsetProfile = (BluetoothHeadset) proxy;
            mHeadsetConnected.setText("true");
        }

        @Override
        public void onServiceDisconnected(int profile) {
            mHeadsetConnected.setText("false");
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRoot = (TableLayout)findViewById(R.id.table_root);
        fillTxt();
        register();
    }

    @Override
    protected void onDestroy() {
        deregister();
        super.onDestroy();
    }

    private void register() {
        BluetoothManager bluetoothManager = (BluetoothManager)getSystemService(BLUETOOTH_SERVICE);
        BluetoothAdapter adapter = bluetoothManager.getAdapter();

        mA2dpServiceListener = new A2DPServiceListener();
        adapter.getProfileProxy(this.getApplicationContext(), mA2dpServiceListener, BluetoothProfile.A2DP);

        mHeadsetServiceListener = new HeadsetServiceListener();
        adapter.getProfileProxy(this.getApplicationContext(), mHeadsetServiceListener, BluetoothProfile.HEADSET);

    }

    private void deregister() {
        BluetoothManager bluetoothManager = (BluetoothManager)getSystemService(BLUETOOTH_SERVICE);
        BluetoothAdapter adapter = bluetoothManager.getAdapter();
        adapter.closeProfileProxy(BluetoothProfile.A2DP, mA2dpProfile);
        adapter.closeProfileProxy(BluetoothProfile.HEADSET, mHeadsetProfile);
    }

    private void fillTxt() {
        addHeader("BluetoothProfile API");
        addHeader("A2DP");
        mA2dpConnected = addRow("connected? ", "");
        addHeader("HEADSET");
        mHeadsetConnected = addRow("connected? ", "");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addHeader(String title) {
        final TextView env = new TextView(this);
        env.setText(title);
        mRoot.addView(env);
    }

    private TextView addRow(String key, String value) {
        final TableRow tr = new TableRow(this);
        final TextView c2 = new TextView(this);
        c2.setText(key);
        final TextView c2val = new TextView(this);
        c2val.setText("" + value);
        tr.addView(c2);
        tr.addView(c2val);
        mRoot.addView(tr);
        return c2val;
    }

}
