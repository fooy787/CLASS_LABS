using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;
using UnityEngine.UI;


//GameObject.Find("Player").transform.Translate();
public class inputScript : MonoBehaviour {

    float curNetWorkConnectionAmount = 0;
    float curAcceleration = 0;
    
    bool onWheel;
  

	// Use this for initialization
	void Start () {
        onWheel = false;
	}

    // Update is called once per frame
    void Update()
    {
        if (curNetWorkConnectionAmount >= 100)
        {
            SceneManager.LoadScene("EndScene");
            Application.Quit();
        }


        if (onWheel == true)
        {
            if (Input.GetKeyDown(KeyCode.RightArrow))
            {
                curNetWorkConnectionAmount++;
                int tempRand = Random.Range(0, 10);
                if(tempRand < 3)
                {
                    onWheel = false;
                    print("random hit");
                }
            }
            
        }
    }

    public void GetOnWheel()
    {
        onWheel = true;
    }

    public bool returnWheelState() { return onWheel; }

    public float returnNetwork() { return curNetWorkConnectionAmount; }
}
