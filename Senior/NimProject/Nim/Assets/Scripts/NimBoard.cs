using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using UnityEngine.SceneManagement;

public class NimBoard : MonoBehaviour {
    public List<GameObject> selectedPieces = new List<GameObject>();
    public GameObject piece;
    public GameObject clone;
    public int NumPieces;
    public GameObject gameState;
    // Use this for initialization
    void Start () {
        NumPieces = 0;
        for (int i = 0; i < 3; i++)
        {
            int val = 5 - i;
            int j = 0;
            while (val > 0)
            { 
                GameObject g = Instantiate(piece, new Vector3((i * 2.0f) - 3, (j * 2.0f * -1) + 3.5f, 0), Quaternion.identity);
                g.GetComponent<NimPiece>().colIdx = i;
                g.GetComponent<NimPiece>().rowIdx = j;
                g.GetComponent<NimPiece>().boardRef = this.gameObject;
                g.GetComponent<NimPiece>().gameState = this.gameState;
                val--;
                j++;
                NumPieces++;
            }
        }
	}
	
	// Update is called once per frame
	void Update () {
        Debug.Log(NumPieces);
        if(NumPieces <= 0)
        {
            bool waited = false;
            float time = 0;
            if (gameState.GetComponent<GameStateManager>().mCurTurn == "AI")
            {
                
                GameObject.Find("winnerText").GetComponent<Text>().text = "You Win!";
                GameObject.Find("winnerText").GetComponent<Text>().enabled = true;
                while(!waited)
                {
                    time += Time.deltaTime / 1000;
                    if(time >= 50)
                    {
                        waited = true;
                    }
                }
                SceneManager.LoadScene(0);

            }
            else
            {
                GameObject.Find("winnerText").GetComponent<Text>().text = "You lose!";
                GameObject.Find("winnerText").GetComponent<Text>().enabled = true;
                while (!waited)
                {
                    time += Time.deltaTime / 1000;
                    if (time >= 50)
                    {
                        waited = true;
                    }
                }
                SceneManager.LoadScene(0);
            }
        }
        else
        {
            GameObject.Find("winnerText").GetComponent<Text>().enabled = false;
        }
	}
    
}
