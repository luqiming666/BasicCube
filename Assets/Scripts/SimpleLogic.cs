using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class SimpleLogic : MonoBehaviour
{
    [Header("Ðý×ªËÙ¶È"), Range(10, 100)]
    public float rotateSpeed = 50f;

    // Start is called before the first frame update
    void Start()
    {
        //transform.localEulerAngles = new Vector3(90, 0, 0);
    }

    // Update is called once per frame
    void Update()
    {
        Vector3 angels = transform.localEulerAngles;
        angels.y += rotateSpeed * Time.deltaTime;
        transform.localEulerAngles = angels;
    }

}
