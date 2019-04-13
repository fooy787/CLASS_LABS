using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using UnityEngine.SceneManagement;

public class sleepScript : MonoBehaviour
{
    levelChanger sn = null;
    // Use this for initialization
    void Start ()
    {
        StartCoroutine(displayInfo());
        GameObject tmp = GameObject.FindWithTag("blackScreen");
        sn = (levelChanger)tmp.GetComponent(typeof(levelChanger));
        Scene scene = SceneManager.GetActiveScene();
        string curScene = scene.name;
        sn.FadeToLevel("insidePlayerHouse", curScene);
    }
	
	// Update is called once per frame
	void Update ()
    {
        
    }

    IEnumerator displayInfo()
    {

        Text mTextBox = GameObject.Find("sleepText").GetComponent<Text>();
        
        mTextBox.text = "You go to sleep";
        yield return new WaitForSeconds(3);
        mTextBox.text = "You wake up";
        yield return new WaitForSeconds(3);
    }
}
