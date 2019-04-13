using System.Collections;
using System.Collections.Generic;
using UnityEngine;


public class cameraRotateScript : MonoBehaviour {
   
    private Vector3 offset;

   
    [Header("Variables")]
    public Transform player;


    [Header("Position")]
    public float camPosX;
    public float camPosY;
    public float camPosZ;

 
    [Header("Rotation")]
    public float camRotationX;
    public float camRotationY;
    public float camRotationZ;

    [Range(0f, 10f)]
    public float turnSpeed;

   
    private void Start()
    {
        offset = new Vector3(player.position.x + camPosX, player.position.y + camPosY, player.position.z + camPosZ);
        transform.rotation = Quaternion.Euler(camRotationX, camRotationY, camRotationZ);
    }


    private void LateUpdate()
    {
        offset = Quaternion.AngleAxis(Input.GetAxis("Mouse X") * turnSpeed, Vector3.up) * Quaternion.AngleAxis( -1 * Input.GetAxis("Mouse Y") * turnSpeed, Vector3.right) * offset;
        transform.position = player.position + offset;
        transform.LookAt(player.position);
    }
}
