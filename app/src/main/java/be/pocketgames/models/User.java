package be.pocketgames.models;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {

    private String mImageProfil;
    private String mFirstname;
    private String mLastname;
    private  String mEmail;
    private Country mCountry;
    private List<GameCollection> mUserGamesCollection;

    public User() {
    }

    public User(String mImageProfil, String mFirstname, String mLastname, String mEmail, Country mCountry) {
        this.mImageProfil = mImageProfil;
        this.mFirstname = mFirstname;
        this.mLastname = mLastname;
        this.mEmail = mEmail;
        this.mCountry = mCountry;
    }

    public User(
                String mImageProfil,
                String mFirstname,
                String mLastname,
                String mEmail,
                Country mCountry,
                List<GameCollection> mUserGamesCollection
    ) {
        this.mImageProfil = mImageProfil;
        this.mFirstname = mFirstname;
        this.mLastname = mLastname;
        this.mEmail = mEmail;
        this.mCountry = mCountry;
        this.mUserGamesCollection = mUserGamesCollection;
    }

    public String getmImageProfil() {
        return mImageProfil;
    }

    public String getmFirstname() {
        return mFirstname;
    }

    public String getmLastname() {
        return mLastname;
    }

    public String getmEmail() {
        return mEmail;
    }

    public Country getmCountry() {
        return mCountry;
    }

    public List<GameCollection> getmUserGamesCollection() {
        return mUserGamesCollection;
    }
}
