import pytest
import requests
import json

BASE_URL = "http://localhost:8080/api/listings/listings"

@pytest.fixture
def headers():
    return {"Content-Type": "application/json"}

def test_create_listing_success(headers):
    payload = {
        "name": "Test Listing",
        "listingType": "SALE",
        "propertyId": "1",
        "description": "A test listing",
        "price": 1000000.0,
        "agentId": "1",
        "bedrooms": 3,
        "bathrooms": 2,
        "area": 120.5,
        "featuredImage": "http://example.com/image.jpg",
        "images": ["http://example.com/image1.jpg", "http://example.com/image2.jpg"]
    }
    response = requests.post(BASE_URL, headers=headers, data=json.dumps(payload))
    
    assert response.status_code == 201
    assert response.json()["data"]["name"] == "Test Listing"
    assert response.json()["data"]["id"] is not None

def test_get_listing_success():
    response = requests.get(f"{BASE_URL}/1")  # Giả sử listing với ID=1 đã tồn tại
    
    assert response.status_code == 200
    assert response.json()["data"]["id"] == "1"

def test_get_listing_not_found():
    response = requests.get(f"{BASE_URL}/999")
    
    assert response.status_code == 404
    assert "message" in response.json()

def test_update_listing_success(headers):
    payload = {
        "name": "Updated Listing",
        "description": "Updated description",
        "price": 1500000.0
    }
    response = requests.put(f"{BASE_URL}/1", headers=headers, data=json.dumps(payload))
    
    assert response.status_code == 200
    assert response.json()["data"]["name"] == "Updated Listing"

def test_delete_listing_success():
    response = requests.delete(f"{BASE_URL}/1")
    
    assert response.status_code == 204

def test_search_listings_success():
    params = {"page": 0, "size": 10, "listingType": "SALE"}
    response = requests.get(BASE_URL, params=params)
    
    assert response.status_code == 200
    assert isinstance(response.json()["data"], list)