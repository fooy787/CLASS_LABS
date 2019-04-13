using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class teleportToInsidePlayerHouse : MonoBehaviour
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
        sn.FadeToLevel("insidePlayerHouse", curScene);

        /*
        SceneManager.LoadScene("insidePlayerHouse");
        //GameObject.Find("player").GetComponent<BoxCollider2D>().enabled = false;
        if (curScene == "outsidePlayerHouse")
        {
            GameObject.Find("player").transform.position = new Vector3(14.47f, 7.43f, 3.0f);
        }
        //GameObject.Find("player").GetComponent<BoxCollider2D>().enabled = true;
        */
    }
}
