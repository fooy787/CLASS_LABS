using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class mainMenuButtons : MonoBehaviour
{

	// Use this for initialization
	void Start ()
    {
		
	}
	
	// Update is called once per frame
	void Update ()
    {
		
	}

    public void goToHouse()
    {
        SceneManager.LoadScene("setup");
    }

    public void goToMainMenu()
    {
        SceneManager.LoadScene("mainMenu");
    }
    public void exitGame()
    {
        Application.Quit();
    }
}
