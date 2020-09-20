/*
 * 	Flan.Zeng 2011-2016	http://git.oschina.net/signup?inviter=flan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.livevideo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * 显示游戏详情的 fragment 适配器
 * @author  zemg
 * @since   2016年5月17日
 */
public class FragmentViewPagerAdapter extends FragmentStatePagerAdapter {

	private ArrayList<Fragment> fragmentList;

	public FragmentViewPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	public void setDate(ArrayList<Fragment> fragmentList) {
		this.fragmentList = fragmentList;
	}

	@Override
	public Fragment getItem(int position) {
		if(fragmentList != null){

			Fragment fragment = fragmentList.get(position);
			return fragment;
		}
		return null;
	}

	@Override
	public int getCount() {
		if(fragmentList != null){
			return fragmentList.size();
		}
		return 0;
	}

}

















