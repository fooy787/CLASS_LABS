using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class bedScript : MonoBehaviour
{
    GameObject go;
    playerMovement other;
    bool byBed = false;
    levelChanger sn = null;


    // Use this for initialization
    void Start ()
    {
        go = GameObject.Find("player");
        other = (playerMovement)go.GetComponent(typeof(playerMovement));
        GameObject tmp = GameObject.FindWithTag("blackScreen");
        sn = (levelChanger)tmp.GetComponent(typeof(levelChanger));
    }

    // Update is called once per frame
    void Update()
    {
        if (Input.GetKeyDown("space") && byBed == true)
        {
            goToSleep();
        }
    }


    public void goToSleep()
    {
        Scene scene = SceneManager.GetActiveScene();
        string curScene = scene.name;
        other.decreaseDay();
        sn.FadeToLevel("sleep", curScene);
    }

    void OnTriggerEnter2D(Collider2D Collider)
    {
        byBed = true;
    }

    void OnTriggerExit2D(Collider2D Collider)
    {
        byBed = false;
    }
}
