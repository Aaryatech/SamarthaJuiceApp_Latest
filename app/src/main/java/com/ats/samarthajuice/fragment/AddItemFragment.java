package com.ats.samarthajuice.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.samarthajuice.BuildConfig;
import com.ats.samarthajuice.R;
import com.ats.samarthajuice.constant.Constants;
import com.ats.samarthajuice.model.Admin;
import com.ats.samarthajuice.model.CategoryModel;
import com.ats.samarthajuice.model.ItemModel;
import com.ats.samarthajuice.util.CommonDialog;
import com.ats.samarthajuice.util.CustomSharedPreference;
import com.ats.samarthajuice.util.PermissionsUtil;
import com.ats.samarthajuice.util.RealPathUtil;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddItemFragment extends Fragment implements View.OnClickListener {

    private EditText edName, edDesc, edRate, edRateSpecial1, edRateSpecial2, edSgst, edCgst, edHSN;
    private ImageView imageView, ivCamera;
    private Button btnSubmit;
    private Spinner spinner;
    private TextView tvImageName;

    int itemId = 0, userId, catId = 0;

    String strName, strDesc, strImage, strHSN,strRate,strParcelRate;
    float rate, specialRate1, specialRate2, sgst, cgst;

    private ArrayList<String> categoryNameList = new ArrayList<>();
    private ArrayList<Integer> categoryIdList = new ArrayList<>();

    //---------------IMAGE----------------
    File folder = new File(Environment.getExternalStorageDirectory() + File.separator, "Happy_Feast");
    File f;

    Bitmap myBitmap = null;
    public static String path, imagePath;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_item, container, false);

        getActivity().setTitle("Item Details");

        if (PermissionsUtil.checkAndRequestPermissions(getActivity())) {

        }

        createFolder();

        Gson gson = new Gson();
        String jsonAdmin = CustomSharedPreference.getString(getContext(), CustomSharedPreference.KEY_ADMIN);
        final Admin admin = gson.fromJson(jsonAdmin, Admin.class);

        Log.e("Admin : ", "---------------------------" + admin);

        if (admin != null) {
            userId = admin.getAdminId();
        }

        edName = view.findViewById(R.id.edName);
        edDesc = view.findViewById(R.id.edDesc);
        edRate = view.findViewById(R.id.edRate);
        edRateSpecial1 = view.findViewById(R.id.edRateSpecial1);
        edRateSpecial2 = view.findViewById(R.id.edRateSpecial2);
        edSgst = view.findViewById(R.id.edSgst);
        edCgst = view.findViewById(R.id.edCgst);
        edHSN = view.findViewById(R.id.edHSN);
        imageView = view.findViewById(R.id.imageView);
        ivCamera = view.findViewById(R.id.ivCamera);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        spinner = view.findViewById(R.id.spinner);
        tvImageName = view.findViewById(R.id.tvImageName);

        btnSubmit.setOnClickListener(this);
        ivCamera.setOnClickListener(this);

        String str = "";
        try {
            str = getArguments().getString("model");
        } catch (Exception e) {
        }

        Gson gson1 = new Gson();
        ItemModel model = gson1.fromJson(str, ItemModel.class);

        Log.e("GSON", "--------------" + model);

        if (model != null) {
            Log.e("MODEL : ", "--------------" + model);

            itemId = model.getItemId();

            edName.setText(model.getItemName());
            edDesc.setText(model.getItemDesc());
            edRate.setText("" + model.getMrpRegular());
            edRateSpecial1.setText("" + model.getMrpSpecial());
            edRateSpecial2.setText("" + model.getMrpGame());
            edSgst.setText("" + model.getSgst());
            edCgst.setText("" + model.getCgst());
            edHSN.setText("" + model.getHsnCode());

            catId = model.getCatId();

            try {
                Picasso.get().load(Constants.itemImagePath + "" + model.getItemImage())
                        .placeholder(R.drawable.logo)
                        .error(R.drawable.logo)
                        .into(imageView);
            } catch (Exception e) {
            }

        }

        getAllCategory();

        return view;
    }

    public void createFolder() {
        if (!folder.exists()) {
            folder.mkdir();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ivCamera) {
            showCameraDialog();

        } else if (v.getId() == R.id.btnSubmit) {

            strName = edName.getText().toString();
            strDesc = edDesc.getText().toString();
            strRate = edRate.getText().toString();
            strParcelRate = edRateSpecial1.getText().toString();


            specialRate2 = Float.parseFloat(edRateSpecial2.getText().toString());
            sgst = Float.parseFloat(edSgst.getText().toString());
            cgst = Float.parseFloat(edCgst.getText().toString());
            strHSN = edHSN.getText().toString();

            int pos = spinner.getSelectedItemPosition();
            catId = categoryIdList.get(pos);

            validate();

        }
    }

    public void validate() {

        if (spinner.getSelectedItemPosition() == 0) {
            TextView view = (TextView) spinner.getSelectedView();
            view.setError("required");
        } else if (strName.isEmpty()) {
            edName.setError("required");
        }else if (strRate.isEmpty()) {
            edName.setError(null);
            edRate.setError("required");
        }else if (strParcelRate.isEmpty()) {
            edName.setError(null);
            edRate.setError(null);
            edRateSpecial1.setError("required");
        } else if (strHSN.isEmpty()) {
            edName.setError(null);
            edRate.setError(null);
            edRateSpecial1.setError(null);
            edHSN.setError("required");
        } else {
            edName.setError(null);
            edRate.setError(null);
            edRateSpecial1.setError(null);
            edHSN.setError(null);

            rate = Float.parseFloat(edRate.getText().toString());
            specialRate1 = Float.parseFloat(edRateSpecial1.getText().toString());


            TextView view = (TextView) spinner.getSelectedView();
            view.setError(null);

            if (imagePath == null) {
                imagePath = "";
            }

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            if (imagePath.isEmpty()) {
                //Toast.makeText(getActivity(), "Please add photo", Toast.LENGTH_SHORT).show();
                strImage = "";
                ItemModel itemModel = new ItemModel(itemId, strName, strDesc, strImage, specialRate2, rate, specialRate1, 0, 0, 0, 0, catId, sgst, cgst, 0, userId, sdf.format(calendar.getTimeInMillis()), 1, 0, strHSN);
                saveItem(itemModel);
            } else {
                File imgFile = new File(imagePath);
                int pos = imgFile.getName().lastIndexOf(".");
                String ext = imgFile.getName().substring(pos + 1);
                strImage = "item_" + System.currentTimeMillis() + "." + ext;

                ItemModel itemModel = new ItemModel(itemId, strName, strDesc, strImage, specialRate2, rate, specialRate1, 0, 0, 0, 0, catId, sgst, cgst, 0, userId, sdf.format(calendar.getTimeInMillis()), 1, 0, strHSN);

                sendImage(strImage, "item", itemModel);

            }


        }

    }


    public void getAllCategory() {

        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<CategoryModel>> listCall = Constants.myInterface.getAllCategory();
            listCall.enqueue(new Callback<ArrayList<CategoryModel>>() {
                @Override
                public void onResponse(Call<ArrayList<CategoryModel>> call, Response<ArrayList<CategoryModel>> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("Category Data : ", "------------" + response.body());

                            ArrayList<CategoryModel> data = response.body();
                            if (data == null) {
                                commonDialog.dismiss();
                                Toast.makeText(getContext(), "No categories found", Toast.LENGTH_SHORT).show();
                            } else {

                                categoryIdList.clear();
                                categoryNameList.clear();

                                categoryIdList.add(0);
                                categoryNameList.add("select category");

                                for (int i = 0; i < data.size(); i++) {
                                    categoryIdList.add(data.get(i).getCatId());
                                    categoryNameList.add(data.get(i).getCatName());
                                }

                                ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, categoryNameList);
                                spinner.setAdapter(adapter);

                                ArrayAdapter<Integer> catIdAdapter = new ArrayAdapter<Integer>(getContext(), android.R.layout.simple_spinner_dropdown_item, categoryIdList);
                                if (catId > 0) {
                                    int spinnerPosition = catIdAdapter.getPosition(catId);
                                    spinner.setSelection(spinnerPosition);
                                }

                                commonDialog.dismiss();
                            }
                        } else {
                            commonDialog.dismiss();
                            Toast.makeText(getContext(), "No categories found", Toast.LENGTH_SHORT).show();
                            Log.e("Data Null : ", "-----------");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        //  Toast.makeText(getContext(), "No categories found", Toast.LENGTH_SHORT).show();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<CategoryModel>> call, Throwable t) {
                    commonDialog.dismiss();
                    Toast.makeText(getContext(), "No categories found", Toast.LENGTH_SHORT).show();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }

    public void showCameraDialog() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
        builder.setTitle("Choose");
        builder.setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent pictureActionIntent = null;
                pictureActionIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pictureActionIntent, 101);
            }
        });
        builder.setNegativeButton("Camera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        f = new File(folder + File.separator, "Camera.jpg");

                        String authorities = BuildConfig.APPLICATION_ID + ".provider";
                        Uri imageUri = FileProvider.getUriForFile(getContext(), authorities, f);

                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivityForResult(intent, 102);

                    } else {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        f = new File(folder + File.separator, "Camera.jpg");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivityForResult(intent, 102);

                    }
                } catch (Exception e) {
                    ////Log.e("select camera : ", " Exception : " + e.getMessage());
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String realPath;
        Bitmap bitmap = null;

        if (resultCode == getActivity().RESULT_OK && requestCode == 102) {
            try {
                String path = f.getAbsolutePath();
                File imgFile = new File(path);
                if (imgFile.exists()) {
                    myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    imageView.setImageBitmap(myBitmap);

                    myBitmap = shrinkBitmap(imgFile.getAbsolutePath(), 720, 720);

                    try {
                        FileOutputStream out = new FileOutputStream(path);
                        myBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                        out.flush();
                        out.close();
                        Log.e("Image Saved  ", "---------------");

                    } catch (Exception e) {
                        Log.e("Exception : ", "--------" + e.getMessage());
                        e.printStackTrace();
                    }
                }
                imagePath = f.getAbsolutePath();
                tvImageName.setText("" + f.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (resultCode == getActivity().RESULT_OK && requestCode == 101) {
            try {
                realPath = RealPathUtil.getRealPathFromURI_API19(getContext(), data.getData());
                Uri uriFromPath = Uri.fromFile(new File(realPath));
                myBitmap = getBitmapFromCameraData(data, getContext());

                imageView.setImageBitmap(myBitmap);
                imagePath = uriFromPath.getPath();
                tvImageName.setText("" + uriFromPath.getPath());

                try {

                    FileOutputStream out = new FileOutputStream(uriFromPath.getPath());
                    myBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                    //Log.e("Image Saved  ", "---------------");

                } catch (Exception e) {
                    // Log.e("Exception : ", "--------" + e.getMessage());
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
                // Log.e("Image Compress : ", "-----Exception : ------" + e.getMessage());
            }
        }
    }


    public static Bitmap getBitmapFromCameraData(Intent data, Context context) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = context.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

        String picturePath = cursor.getString(columnIndex);
        path = picturePath;
        cursor.close();

        Bitmap bitm = shrinkBitmap(picturePath, 720, 720);
        Log.e("Image Size : ---- ", " " + bitm.getByteCount());

        return bitm;
        // return BitmapFactory.decodeFile(picturePath, options);
    }

    public static Bitmap shrinkBitmap(String file, int width, int height) {
        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
        bmpFactoryOptions.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);

        int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight / (float) height);
        int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth / (float) width);

        if (heightRatio > 1 || widthRatio > 1) {
            if (heightRatio > widthRatio) {
                bmpFactoryOptions.inSampleSize = heightRatio;
            } else {
                bmpFactoryOptions.inSampleSize = widthRatio;
            }
        }

        bmpFactoryOptions.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
        return bitmap;
    }

    private void sendImage(String filename, String type, final ItemModel bean) {
        final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
        commonDialog.show();

        File imgFile = new File(imagePath);

        RequestBody requestFile = RequestBody.create(MediaType.parse("image"), imgFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", imgFile.getName(), requestFile);

        RequestBody imgName = RequestBody.create(MediaType.parse("text/plain"), filename);
        RequestBody imgType = RequestBody.create(MediaType.parse("text/plain"), type);


        Call<JSONObject> call = Constants.myInterface.imageUpload(body, imgName, imgType);
        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                commonDialog.dismiss();
                saveItem(bean);
                imagePath = "";
                Log.e("Response : ", "--" + response.body());
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                Log.e("Error : ", "--" + t.getMessage());
                commonDialog.dismiss();
                t.printStackTrace();
                Toast.makeText(getActivity(), "Unable To Process", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void saveItem(ItemModel itemModel) {
        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ItemModel> listCall = Constants.myInterface.saveItem(itemModel);
            listCall.enqueue(new Callback<ItemModel>() {
                @Override
                public void onResponse(Call<ItemModel> call, Response<ItemModel> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("Admin Data : ", "------------" + response.body());

                            ItemModel data = response.body();
                            if (data == null) {
                                commonDialog.dismiss();
                                Toast.makeText(getContext(), "Unable to process!", Toast.LENGTH_SHORT).show();
                            } else {

                                Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                                commonDialog.dismiss();

                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.content_frame, new ItemMasterFragment(), "HomeFragment");
                                ft.commit();

                            }
                        } else {
                            commonDialog.dismiss();
                            Toast.makeText(getContext(), "Unable to process!", Toast.LENGTH_SHORT).show();
                            Log.e("Data Null : ", "-----------");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Toast.makeText(getContext(), "Unable to process!", Toast.LENGTH_SHORT).show();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ItemModel> call, Throwable t) {
                    commonDialog.dismiss();
                    Toast.makeText(getContext(), "Unable to process!", Toast.LENGTH_SHORT).show();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }


}
