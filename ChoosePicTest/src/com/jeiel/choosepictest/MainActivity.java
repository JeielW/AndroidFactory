public class MainActivity extends Activity{
	private static final String TAKE_PHOTO = 0;
	private static final String CROP_PHOTO = 1;
	private Button takePhoto;
	private Button chooseFromAlbum;
	private ImageView picture;
	private Uri imageUri;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		takePhoto = (Button) findViewById(R.id.take_photo);
		chooseFromAlbum = (Button) findViewById(R.id.choose_from_album);
		picture = (Button) findViewById(R.id.picture);

		takePhoto.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				File outputImage = new File(Environment.getExternalStorageDirectory(), "output_image.jpg");
				try{
					if(outputImage.exists()){
						outputImage.delete()
					}
					outputImage.createNewFile();
				}catch(IOException e){
					e.printStackTrace();
				}
				imageUri = Uri.fromFile(outputImage);
				Intent intent = new Intent("android.media.ation.IMAGE_CAPTURE");
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent, TAKE_PHOTO);
			}
		});

		chooseFromAlbum.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				File outputImage = new File(Environment.getExternalStorageDirectory(), "output_image.jpg");
				try{
					if(outputImage.exists()){
						outputImage.delete();
					}
					outputImage.createNewFile();
				}
				imageUri = Uri.fromFile(outputImage);
				Intent intent = new Intent("android.intent.action.GET_CONTENT");
				intent.setType("image/*");
				intent.putExtra("crop", true);
				intent.putExtra("scale", true);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent, CROP_PHOTO);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		switch(requestCode){
		case TAKE_PHOTO:
			if(resultCode == RESULT_OK){
				Intent intent = new Intent("com.android.camera.action.CROP");
				intent.setDataAndType(imageUri, "image/*");
				intent.putExtra("scale", true);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent, CROP_PHOTO);
			}
			break;
		case CROP_PHOTO:
			if(resultCode == RESULT_OK){
				try{
					Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().
						openInputStrem(imageUri));
					picture.setImageBitmap(bitmap);
				}catch(FileNotFoundException e){
					e.printStackTrace();
				}
				
			}
			break;
		default:
			break;
		}
	}
}