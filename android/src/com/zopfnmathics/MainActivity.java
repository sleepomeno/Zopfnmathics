package com.zopfnmathics;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements ActionBar.OnNavigationListener {

    private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        // Set up the dropdown list navigation in the action bar.
        actionBar.setListNavigationCallbacks(
                // Specify a SpinnerAdapter to populate the dropdown list.
                new ArrayAdapter(
                        actionBar.getThemedContext(),
                        android.R.layout.simple_list_item_1,
                        android.R.id.text1,
                        new String[]{
                                getString(R.string.title_section1),
                                getString(R.string.title_section2),
                                getString(R.string.title_section3),
                        }),
                this);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
            getActionBar().setSelectedNavigationItem(
                    savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_SELECTED_NAVIGATION_ITEM,
                getActionBar().getSelectedNavigationIndex());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    


    public boolean onNavigationItemSelected(int position, long id) {
        // When the given tab is selected, show the tab contents in the container
    	Fragment fragment = null;
    	switch(position) {
    	case 0:
    		fragment = new ViewZopfnFragment();
    		break;
    	default: fragment = createDummyFragment(position);;
    	}
    	
        
        getSupportFragmentManager().beginTransaction()
        .replace(R.id.container, fragment).commit();
        
        return true;
    }

	private Fragment createDummyFragment(int position) {
		Fragment fragment = new DummySectionFragment();
        Bundle args = new Bundle();
        args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
        fragment.setArguments(args);
		return fragment;
	}
	
	public class ViewZopfnFragment extends Fragment {
		private List<String> calcLines = new ArrayList<String>();
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			
			final View view = inflater.inflate(R.layout.view_zopfn_fragment, container, false);
			final EditText zopfnEditText = (EditText) view.findViewById(R.id.number);
			final Button calculateButton = (Button) view.findViewById(R.id.calculate_button);
			final Button clearButton = (Button) view.findViewById(R.id.clear_button);
			final ListView listView = (ListView) view.findViewById(R.id.list);
			
			final ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.calc_line, R.id.calc_line, calcLines);
			listView.setAdapter(adapter);
			
			calculateButton.setOnClickListener(new OnClickListener() {		
				
				public void onClick(View v) {
					 int number = Integer.valueOf(zopfnEditText.getText().toString());
					 calcLines.clear();
					 calcLines.addAll(computeCalcLines(number));
					 adapter.notifyDataSetChanged();
					 
//					 Toast.makeText(MainActivity.this.getApplicationContext(), String.valueOf(adapter.getCount()), Toast.LENGTH_SHORT ).show();
				}
			});
			clearButton.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					zopfnEditText.setText("");
					calcLines.clear();
				}
			});
			
			return view;
		}

		protected List<String> computeCalcLines(int number) {
			List<String> lines = new ArrayList<String>();
			lines.add(computeMultiplyCalcLine(number, 1));
			for(int i=2; i<=9; i++) {
				number = number*i;
				lines.add(computeMultiplyCalcLine(number, i));
			}
			
			for(int i=2; i<=9; i++) {
				number = number/i;
				lines.add(computeDividingCalcLine(number, i));
			}
			return lines;
		}
		
		private String computeMultiplyCalcLine(int result, int multiplier) {
			String nextOp = "|  * " + (multiplier+1);
			if(multiplier == 9) {
				nextOp = "|  / 2";
			}
			return String.format("%d   %s", result, nextOp);
		}
		
		private String computeDividingCalcLine(int result, int divider) {
			String nextOp = "|  / " + (divider+1);
			if(divider == 9) {
				nextOp = "         ";
			}
			return String.format("%d   %s", result, nextOp);
		}
	}

    /**
     * A dummy fragment representing a section of the app, but that simply displays dummy text.
     */
    public static class DummySectionFragment extends Fragment {
        public DummySectionFragment() {
        }

        public static final String ARG_SECTION_NUMBER = "section_number";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            TextView textView = new TextView(getActivity());
            textView.setGravity(Gravity.CENTER);
            Bundle args = getArguments();
//            textView.setText(Integer.toString(args.getInt(ARG_SECTION_NUMBER)));
            textView.setText("Not yet implemented");
            return textView;
        }
    }
}
