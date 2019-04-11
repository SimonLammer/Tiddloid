package indi.donmor.tiddloid.utils;

import android.content.Context;
import android.os.Environment;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileFilter;
import java.text.SimpleDateFormat;
import java.util.Date;

import indi.donmor.tiddloid.MainActivity;
import indi.donmor.tiddloid.R;

public class WikiListAdapter extends RecyclerView.Adapter<WikiListAdapter.WikiListHolder> {

	Context context;
	JSONObject db;
	int count;
	ReloadListener rl;
	private LayoutInflater inflater;

	Vibrator vibrator;

	public WikiListAdapter(Context context, JSONObject db) {
		this.context = context;
		this.db = db;
		vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		try {
			count = db.getJSONArray("wiki").length();
		} catch (Exception e) {
			e.printStackTrace();
		}
		inflater = LayoutInflater.from(context);
//		mReloadListener.onReloaded(this.getItemCount());
	}

	public class WikiListHolder extends RecyclerView.ViewHolder {
		private Button btnWiki;
		private CardView cvWiki;
		private String id, path;

		public WikiListHolder(View itemView) {
			super(itemView);
			btnWiki = (Button) itemView.findViewById(R.id.btnWiki);
			cvWiki = (CardView) itemView.findViewById(R.id.cvWiki);
		}
	}

	@Override
	public WikiListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		WikiListHolder holder = new WikiListHolder(inflater.inflate(R.layout.wiki_slot, parent, false));
		return holder;
	}

	@Override
	public void onBindViewHolder(@NonNull WikiListHolder holder, final int position) {

		try {
			holder.id = db.getJSONArray("wiki").getJSONObject(position).getString("id");
			holder.btnWiki.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mItemClickListener.onItemClick(position);
				}
			});
			holder.btnWiki.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
//                    mItemClickListener.onItemClick(position);
					vibrator.vibrate(new long[]{0,1}, -1);
					Toast.makeText(context, "e", Toast.LENGTH_SHORT).show();
					mItemClickListener.onItemLongClick(position);
					return true;
				}
			});
			holder.path = db.getJSONArray("wiki").getJSONObject(position).getString("path");
			File f = new File(holder.path);
			System.out.println(f.getAbsolutePath());
			if (f.exists()) {
				holder.cvWiki.setVisibility(View.VISIBLE);
				holder.btnWiki.setText(Html.fromHtml("</b>" + db.getJSONArray("wiki").getJSONObject(position).getString("name") + "</b><br><font color=\"grey\">" + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(f.lastModified())) + "</font>"));
			} else holder.cvWiki.setVisibility(View.GONE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public int getItemCount() {
		return count;
	}

	private ItemClickListener mItemClickListener;

	public interface ItemClickListener {
		void onItemClick(int position);

		void onItemLongClick(int position);
	}

	public void setOnItemClickListener(ItemClickListener itemClickListener) {
		this.mItemClickListener = itemClickListener;
	}

	private ReloadListener mReloadListener;

	public interface ReloadListener {
		public void onReloaded(int count);
	}

	public void setReloadListener(ReloadListener reloadListener) {
		this.mReloadListener = reloadListener;
	}

	public void reload(JSONObject db) {
		this.db = db;
		try {
			count = this.db.getJSONArray("wiki").length();
			System.out.println(count);
		} catch (Exception e) {
			e.printStackTrace();
		}
		mReloadListener.onReloaded(this.getItemCount());
	}

	public String getId(int position) {
		String id = null;
		try {
			id = db.getJSONArray("wiki").getJSONObject(position).getString("id");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}
}
