package com.mm.android.deviceaddmodule.mobilecommon.widget.sticky.stickygridheaders;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

/**
 * Base adapter interface for StickyGridHeadersGridView. The adapter expects two sets of data,
 * items, and headers. Implement this interface to provide an optimised method for generating the
 * header data set. Otherwise see {@link StickyGridHeadersSimpleAdapter} for a solution which will
 * auto-generate the set of headers.
 */
public interface StickyGridHeadersBaseAdapter extends ListAdapter {
    /**
     * Get the number of items with a given header.
     * 
     * @return The number of items for the specified header.
     */
    public int getCountForHeader(int header);

    /**
     * Get the number of headers in the adapter's data set.
     * 
     * @return Number of headers.
     */
    public int getNumHeaders();

    /**
     * Get a View that displays the header data at the specified position in the set. You can either
     * create a View manually or inflate it from an XML layout file.
     * 
     * @param position
     *            The position of the header within the adapter's header data set.
     * @param convertView
     *            The old view to reuse, if possible. Note: You should check that this view is
     *            non-null and of an appropriate type before using. If it is not possible to convert
     *            this view to display the correct data, this method can create a new view.
     * @param parent
     *            The parent that this view will eventually be attached to.
     * @return A View corresponding to the data at the specified position.
     */
    View getHeaderView(int position, View convertView, ViewGroup parent);
}
