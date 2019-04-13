using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class MenuScript : MonoBehaviour {
    public GameObject Level;
    public GameObject IGMenu;
    public KeyCode menuKey;
    private float storedTime = 0;

    public void Exit() {
        Application.Quit();
    }

    public void LoadScene(string levelName) {
        SceneManager.LoadScene(levelName);
    }

    public void Update() {
        if (Input.GetKeyDown(menuKey)) {
            setMenuActive(true);
        }
    }

    public void ToggleMenu()
    {
        setMenuActive(!IGMenu.activeInHierarchy);
    }

    public void setMenuActive(bool menuActive) {
        if (menuActive)
        {
            storedTime = Time.timeScale;
            Time.timeScale = 0;
            IGMenu.SetActive(true);
           // Level.SetActive(false);
        }
        else
        {
            IGMenu.SetActive(false);
            Level.SetActive(true);
            Time.timeScale = storedTime;
        }
    }
}
