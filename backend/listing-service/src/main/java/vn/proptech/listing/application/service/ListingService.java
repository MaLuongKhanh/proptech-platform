package vn.proptech.listing.application.service;

import vn.proptech.listing.application.dto.Input.AddListingRequest;
import vn.proptech.listing.application.dto.Input.GetListingRequest;
import vn.proptech.listing.application.dto.Input.UpdateListingRequest;
import vn.proptech.listing.application.dto.Output.GetListingResponse;

import java.util.List;
import java.util.Optional;

public interface ListingService {

    // Create a new listing
    GetListingResponse createListing(AddListingRequest request);

    // Get all listings with filter
    List<GetListingResponse> getAllListings(GetListingRequest request);

    // Get a listing by ID
    Optional<GetListingResponse> getListingById(String id);

    // Update a listing
    GetListingResponse updateListing(String id, UpdateListingRequest request);

    // Delete a listing
    boolean deleteListing(String id);

    // Find listings by agent ID with pagination
    List<GetListingResponse> findByAgentId(String agentId, GetListingRequest request);
    
    // Find listings by location
    List<GetListingResponse> findByLocation(double latitude, double longitude, double maxDistanceKm);

    // Find listings by address keyword
    List<GetListingResponse> findByAddressKeyword(String keyword);

    // Count search results based on criteria
    long countSearchResults(GetListingRequest request);
}