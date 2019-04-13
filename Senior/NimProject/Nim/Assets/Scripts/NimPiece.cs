using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class NimPiece : MonoBehaviour {
    public int colIdx;
    public int rowIdx;
    public bool selected;
    public GameObject gameState;
    public GameObject boardRef;
    public Color baseColor;
	// Use this for initialization
	void Start () {
        selected = false;
        baseColor = this.GetComponent<SpriteRenderer>().material.color;
	}
	
	// Update is called once per frame
	void Update () {
	    if (selected)
        {
            this.GetComponent<SpriteRenderer>().material.SetColor("_Color", Color.red);
        }
        else
        {
            this.GetComponent<SpriteRenderer>().material.SetColor("_Color", this.baseColor);
        }
        if (gameState.GetComponent<GameStateManager>().mCurTurn == "AI")
        {
            if (selected)
            {
                boardRef.GetComponent<NimBoard>().NumPieces--;
                boardRef.GetComponent<NimBoard>().selectedPieces.Remove(this.gameObject);
                Destroy(this.gameObject);
            }
        }

	}
    void OnMouseDown()
    {
        if (!gameState.GetComponent<GameStateManager>().canPress)
        {
            return;
        }

        if (gameState.GetComponent<GameStateManager>().mCurTurn == "Player")
        {
            selected = !selected;
            if (selected)
            {
                if (boardRef.GetComponent<NimBoard>().selectedPieces.Count == 0)
                {
                    boardRef.GetComponent<NimBoard>().selectedPieces.Add(gameObject);
                }
                else if (boardRef.GetComponent<NimBoard>().selectedPieces.Count > 0)
                {
                    GameObject tmpgobj = boardRef.GetComponent<NimBoard>().selectedPieces[0];
                    if (tmpgobj.GetComponent<NimPiece>().colIdx == this.colIdx)
                    {
                        boardRef.GetComponent<NimBoard>().selectedPieces.Add(gameObject);
                    }
                    else
                    {
                        for (int i = 0; i < boardRef.GetComponent<NimBoard>().selectedPieces.Count; i++)
                        {
                            boardRef.GetComponent<NimBoard>().selectedPieces[i].GetComponent<NimPiece>().selected = false;
                        }
                        boardRef.GetComponent<NimBoard>().selectedPieces.Clear();
                        boardRef.GetComponent<NimBoard>().selectedPieces.Add(gameObject);
                    }
                }
            }
            else
            {
                boardRef.GetComponent<NimBoard>().selectedPieces.Remove(gameObject);
            }
        }
        
        
    }

}
