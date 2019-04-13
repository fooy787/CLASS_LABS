using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class teleportToTown : MonoBehaviour
{
    levelChanger sn = null;

    // Use this for initialization
    void Start ()
    {
        GameObject tmp = GameObject.FindWithTag("blackScreen");
        sn = (levelChanger)tmp.GetComponent(typeof(levelChanger));
    }
	
	// Update is called once per frame
	void Update ()
    {
	
	}

    void OnCollisionEnter2D(Collision2D Collider)
    {
        Scene scene = SceneManager.GetActiveScene();
        string curScene = scene.name;
        sn.FadeToLevel("town", curScene);

    }
}
