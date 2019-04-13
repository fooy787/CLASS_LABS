using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class ShopeprSpawner : MonoBehaviour
{
    public int MaxShoppers;
    public int MinShoppers;
    public int startingShoppers;

    public Transform pointsOfIntrest;
    public Transform entry;
    public Transform cashier;

    public float spawnFrequency;
    public float spawnVarincce;

    public int avgPoints;
    public int pointVarince;

    public GameObject shopperPrefab;

    private GameObject shoppers;
    private Transform[] points;
    public float timeToNextspawn;

    void Start()
    {
        shoppers = new GameObject();
        shoppers.name = "shopers";
        shoppers.transform.SetParent(transform);

        points = new Transform[pointsOfIntrest.childCount];

        for (int i = 0; i < points.Length; i++) {
            points[i] = pointsOfIntrest.GetChild(i);
        }

        for (int j = 0; j < startingShoppers; j++) {
            spawnShopper(points[Random.Range(0, points.Length - 1)]);
        }

        timeToNextspawn = spawnFrequency + Random.Range(-spawnVarincce, spawnVarincce);
    }

    void Update()
    {
        //check for spawn
        if (shoppers.transform.childCount < MaxShoppers || MaxShoppers < 0) {
            if (shoppers.transform.childCount > MinShoppers) {
                timeToNextspawn -= Time.deltaTime;
                if (timeToNextspawn <= 0) {
                    timeToNextspawn = spawnFrequency + Random.Range(-spawnVarincce, spawnVarincce);
                    spawnShopper();
                }
            }else
            {
                spawnShopper();
            }
        }
    }

    public ShopperAI spawnShopper()
    {
        return spawnShopper(entry);
    }

    public ShopperAI spawnShopper(Transform start)
    {
        ShopperAI ret = GameObject.Instantiate(shopperPrefab,start.position,start.rotation,shoppers.transform).GetComponent<ShopperAI>();
        Transform[] poi = new Transform[Mathf.Min(avgPoints+ Random.Range(-pointVarince, pointVarince), points.Length)+2];
        for (int i = 0; i < poi.Length-1; i++) {
            Transform temp = points[Random.Range(0, points.Length - 1)];
            poi[i] = temp;
        }
        poi[poi.Length - 2] = cashier;
        poi[poi.Length - 1] = entry;
        ret.init(poi);
        return ret;
    }
}