using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class teleportToOutsideHouse : MonoBehaviour
{
    levelChanger sn = null;
    

    void Start()
    {

        GameObject tmp = GameObject.FindWithTag("blackScreen");
        sn = (levelChanger)tmp.GetComponent(typeof(levelChanger));

    }

    void OnCollisionEnter2D(Collision2D Collider)
    {   
        Scene scene = SceneManager.GetActiveScene();
        string curScene = scene.name;
        //GameObject.Find("player").GetComponent<Rigidbody2D>().isKinematic = false;
        //System.Threading.Thread.Sleep(1000);
        sn.FadeToLevel("outsidePlayerHouse", curScene);

    }
}
