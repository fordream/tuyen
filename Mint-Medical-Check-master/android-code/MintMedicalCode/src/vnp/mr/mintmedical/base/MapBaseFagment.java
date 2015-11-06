package vnp.mr.mintmedical.base;

import android.support.v4.app.Fragment;

import com.google.android.gms.maps.SupportMapFragment;

//com.google.android.gms.maps.MapFragment
public class MapBaseFagment extends SupportMapFragment {
	private Fragment parent;

	public void setParent(Fragment parent) {
		this.parent = parent;
	}

	public Fragment getParent() {
		return parent;
	}
	public MapBaseFagment() {
		super();
	}

}