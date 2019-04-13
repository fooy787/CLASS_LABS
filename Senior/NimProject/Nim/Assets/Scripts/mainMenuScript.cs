using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;
using UnityEngine.UI;

public class mainMenuScript : MonoBehaviour {
    public Slider mSlider;
    public void playGame()
    {
        DontDestroyOnLoad(GameObject.Find("mCanvas"));
        //mSlider.gameObject.SetActive(false);
        SceneManager.LoadScene(1);
        Debug.Log(mSlider.value);
    }

    public void quitGame()
    {
        Application.Quit();
    }
}
