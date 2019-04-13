using System.Collections;
using System.Collections.Generic;
using UnityEngine.SceneManagement;
using UnityEngine;

public class gui : MonoBehaviour {

	// Use this for initialization
	void Start ()
    {	
	}

    // Update is called once per frame
	void Update ()
    {
    }

    public void DoExitGame()
        {
            Application.Quit();
            Debug.Log("Game exiting");
        }

    public void PlayButton()
    {
        SceneManager.LoadScene("SceneOne");
    }
}
