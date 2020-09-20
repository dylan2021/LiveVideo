package com.android.livevideo.widget.pulllistview;



public interface ILoadingLayout {

    public enum State {
        
        /**
         * Initial state
         */
        NONE,
        
        /**
         * When the UI is in a state which means that user is not interacting
         * with the Pull-to-Refresh function.
         */
        RESET,
        
        /**
         * When the UI is being pulled by the user, but has not been pulled far
         * enough so that it refreshes when released.
         */
        PULL_TO_REFRESH,
        
        /**
         * When the UI is being pulled by the user, and <strong>has</strong>
         * been pulled far enough so that it will refresh when released.
         */
        RELEASE_TO_REFRESH,
        
        /**
         * When the UI is currently refreshing, caused by a pull gesture.
         */
        REFRESHING,
        
        /**
         * When the UI is currently refreshing, caused by a pull gesture.
         */
        @Deprecated
        LOADING,
        
        /**
         * No more data
         */
        NO_MORE_DATA, State,
    }

    /**
     * 璁剧疆褰撳墠鐘舵�锛屾淳鐢熺被搴旇鏍规嵁杩欎釜鐘舵�鐨勫彉鍖栨潵鏀瑰彉View鐨勫彉鍖�
     * 
     * @param state 鐘舵�
     */
    public void setState(State state);
    
    /**
     * 寰楀埌褰撳墠鐨勭姸鎬�
     *  
     * @return 鐘舵�
     */
    public State getState();
    
    /**
     * 寰楀埌褰撳墠Layout鐨勫唴瀹瑰ぇ灏忥紝瀹冨皢浣滀负涓�釜鍒锋柊鐨勪复鐣岀偣
     * 
     * @return 楂樺害
     */
    public int getContentSize();
    
    /**
     * 鍦ㄦ媺鍔ㄦ椂璋冪敤
     * 
     * @param scale 鎷夊姩鐨勬瘮渚�
     */
    public void onPull(float scale);
}
