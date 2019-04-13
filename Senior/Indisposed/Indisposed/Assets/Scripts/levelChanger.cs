using UnityEngine;
using UnityEngine.SceneManagement;
using System.Collections;

public class levelChanger : MonoBehaviour
{
    string goTo;
    string comeFrom;
    int something = 0;
    static bool canMove = true;

    public Animator animator;
    
    public bool canPlayerMove()
    {
        return canMove;
    }

    void Start()
    {
        animator = gameObject.GetComponent<Animator>();
    }

    public void FadeToLevel(string goToLevel, string comeFromLevel)
    {
        goTo = goToLevel;
        comeFrom = comeFromLevel;
        canMove = false;
        animator.SetTrigger("fadeOut");
    }

    public void OnFadeComplete()
    {
        ///Debug.Log("gets in fade complete");
        ///Debug.Log(goTo.ToString());
        ///Debug.Log(comeFrom.ToString());
        if (goTo == "outsidePlayerHouse")
        {
            if(comeFrom == "insidePlayerHouse")
            {
                GameObject.Find("player").transform.position = new Vector3(-16.09f, 18.046f, 11.437f);
            }
            else if(comeFrom == "town")
            {
                GameObject.Find("player").transform.position = new Vector3(31.0f, 5.0f, 2.0f);
            }
        }
        else if(goTo == "forest")
        {
            if(comeFrom == "town")
            {
                GameObject.Find("player").transform.position = new Vector3(53.0f, 42.0f, 18.0f);
            }
        }
        else if(goTo == "insidePlayerHouse")
        {
            if(comeFrom == "outsidePlayerHouse")
            {
                Debug.Log("gets inside house from outside");
                GameObject.Find("player").transform.position = new Vector3(13.9f,8.29f, 3.145f);
            }
            else if (comeFrom == "sleep")
            {
                GameObject.Find("player").transform.position = new Vector3(16.0f, 15.0f, 8.5f);
            }
            else
            {
               GameObject.Find("player").transform.position = new Vector3(16.0f, 15.0f, 8.5f);
            }
        }
        else if(goTo == "town")
        {
            if(comeFrom == "outsidePlayerHouse")
            {
                GameObject.Find("player").transform.position = new Vector3(-23.33f, 0.2f, 9.0f);
            }
            else if(comeFrom == "forest")
            {
                GameObject.Find("player").transform.position = new Vector3(92.0f, 14.0f, 9.0f);
            }
        }
        GameObject.Find("player").GetComponent<playerMovement>().moveSet("up", true);
        GameObject.Find("player").GetComponent<playerMovement>().moveSet("down", true);
        GameObject.Find("player").GetComponent<playerMovement>().moveSet("right", true);
        GameObject.Find("player").GetComponent<playerMovement>().moveSet("left", true);
        SceneManager.LoadScene(goTo);
        canMove = true;
        
    }
}
