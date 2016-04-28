package com.tsl.baseapp.model.Objects.project;

import org.json.JSONObject;

/**
 * Created by Kevin on 9/17/15.
 */
public class Project {

    //pojo getters and setters

    public int mId;
    public int mUser;
    public String mName;
    public String mSlug;
    public String mDescription;
    public String mTagline;
    public String mBlurb;
    public String mRisks;
    public String mPlan;
    public JSONObject mAddress;
    public String mCity;
    public String mState;
    public int mCurrency;
    public int mPayoutAccount;
    public String mGoal;
    public String mTotalFunded;
    public int mBackerCount;
    public int mCategory;
    public String mStartDate;
    public String mEndDate;
    public String mImageUrl;
    public String mBannerUrl;
    public String mThankYouImageUrl;
    public String mYoutubeVideoUrl;
    public String mVideoUrl;
    // public ArrayList mPerks; - Find out what type
    public int[] mTagList;
    public String mFaq;
    public String mFacebookUrl;
    public String mTwitterUrl;
    public String mInstagramUrl;
    public String mVoteStatus;
    public int mUpVoteCount;
    public int mDownVoteCounts;

    public String getVoteStatus() {
        return mVoteStatus;
    }

    public void setVoteStatus(String voteStatus) {
        mVoteStatus = voteStatus;
    }

    public boolean isStarted() {
        return mIsStarted;
    }

    public void setIsStarted(boolean isStarted) {
        mIsStarted = isStarted;
    }

    public boolean isSuccess() {
        return mIsSuccess;
    }

    public void setIsSuccess(boolean isSuccess) {
        mIsSuccess = isSuccess;
    }

    public boolean isEnded() {
        return mIsEnded;
    }

    public void setIsEnded(boolean isEnded) {
        mIsEnded = isEnded;
    }

    public int getDownVoteCounts() {
        return mDownVoteCounts;
    }

    public void setDownVoteCounts(int downVoteCounts) {
        mDownVoteCounts = downVoteCounts;
    }

    public int getUpVoteCount() {
        return mUpVoteCount;
    }

    public void setUpVoteCount(int upVoteCount) {
        mUpVoteCount = upVoteCount;
    }

    public String getInstagramUrl() {
        return mInstagramUrl;
    }

    public void setInstagramUrl(String instagramUrl) {
        mInstagramUrl = instagramUrl;
    }

    public String getTwitterUrl() {
        return mTwitterUrl;
    }

    public void setTwitterUrl(String twitterUrl) {
        mTwitterUrl = twitterUrl;
    }

    public String getFacebookUrl() {
        return mFacebookUrl;
    }

    public void setFacebookUrl(String facebookUrl) {
        mFacebookUrl = facebookUrl;
    }

    public String getFaq() {
        return mFaq;
    }

    public void setFaq(String faq) {
        mFaq = faq;
    }

    public String getVideoUrl() {
        return mVideoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        mVideoUrl = videoUrl;
    }

    public int[] getTagList() {
        return mTagList;
    }

    public void setTagList(int[] tagList) {
        mTagList = tagList;
    }

    public String getYoutubeVideoUrl() {
        return mYoutubeVideoUrl;
    }

    public void setYoutubeVideoUrl(String youtubeVideoUrl) {
        mYoutubeVideoUrl = youtubeVideoUrl;
    }

    public String getThankYouImageUrl() {
        return mThankYouImageUrl;
    }

    public void setThankYouImageUrl(String thankYouImageUrl) {
        mThankYouImageUrl = thankYouImageUrl;
    }

    public String getBannerUrl() {
        return mBannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        mBannerUrl = bannerUrl;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public boolean mIsStarted;
    public boolean mIsEnded;

    public int getPayoutAccount() {
        return mPayoutAccount;
    }

    public void setPayoutAccount(int payoutAccount) {
        mPayoutAccount = payoutAccount;
    }

    public String getGoal() {
        return mGoal;
    }

    public void setGoal(String goal) {
        mGoal = goal;
    }

    public String getTotalFunded() {
        return mTotalFunded;
    }

    public void setTotalFunded(String totalFunded) {
        mTotalFunded = totalFunded;
    }

    public int getBackerCount() {
        return mBackerCount;
    }

    public void setBackerCount(int backerCount) {
        mBackerCount = backerCount;
    }

    public String getStartDate() {
        return mStartDate;
    }

    public void setStartDate(String startDate) {
        mStartDate = startDate;
    }

    public String getEndDate() {
        return mEndDate;
    }

    public void setEndDate(String endDate) {
        mEndDate = endDate;
    }

    public boolean mIsSuccess;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getUser() {
        return mUser;
    }

    public void setUser(int user) {
        mUser = user;
    }

    public String getSlug() {
        return mSlug;
    }

    public void setSlug(String slug) {
        mSlug = slug;
    }

    public String getTagline() {
        return mTagline;
    }

    public void setTagline(String tagline) {
        mTagline = tagline;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getBlurb() {
        return mBlurb;
    }

    public void setBlurb(String blurb) {
        mBlurb = blurb;
    }

    public String getPlan() {
        return mPlan;
    }

    public void setPlan(String plan) {
        mPlan = plan;
    }

    public String getRisks() {
        return mRisks;
    }

    public void setRisks(String risks) {
        mRisks = risks;
    }

    public JSONObject getAddress() {
        return mAddress;
    }

    public void setAddress(JSONObject address) {
        mAddress = address;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        mCity = city;
    }

    public String getState() {
        return mState;
    }

    public void setState(String state) {
        mState = state;
    }

    public int getCurrency() {
        return mCurrency;
    }

    public void setCurrency(int currency) {
        mCurrency = currency;
    }


    public int getCategory() {
        return mCategory;
    }

    public void setCategory(int category) {
        mCategory = category;
    }

}
