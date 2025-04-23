package vn.proptech.listing.application.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.proptech.listing.application.dto.Input.AddListingRequest;
import vn.proptech.listing.application.dto.Input.GetListingRequest;
import vn.proptech.listing.application.dto.Input.UpdateListingRequest;
import vn.proptech.listing.application.dto.Output.GetListingResponse;
import vn.proptech.listing.application.dto.Output.GetPropertyResponse;
import vn.proptech.listing.application.extension.CommonCloudinaryAttribute;
import vn.proptech.listing.application.extension.CommonFileType;
import vn.proptech.listing.application.mapper.Input.AddListingRequestMapper;
import vn.proptech.listing.application.mapper.Output.GetListingResponseMapper;
import vn.proptech.listing.domain.model.Listing;
import vn.proptech.listing.domain.model.ListingType;
import vn.proptech.listing.domain.repository.ListingRepository;
import vn.proptech.listing.domain.service.ListingService;
import vn.proptech.listing.domain.service.PropertyService;
import vn.proptech.listing.infrastructure.messaging.ListingEventPublisher;

import java.io.File;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ListingServiceImpl implements ListingService {

    private final ListingRepository listingRepository;
    private final ListingEventPublisher eventPublisher;
    private final PropertyService propertyService;
    private final Cloudinary cloudinary;

    @Override
    public GetListingResponse createListing(AddListingRequest addListingRequest) {
        log.info("Creating new listing: {}", addListingRequest.getName());
        try {
            String newId = UUID.randomUUID().toString();
            String assetFolderImage = CommonCloudinaryAttribute.assetFolderListing;
            String publicId = Listing.class.getSimpleName() + "_" + newId;

            // upload feature image
            String featuredImageUrl = uploadImageToCloudinary(
                    addListingRequest.getFeaturedImage(),
                    assetFolderImage,
                    publicId + "_featured",
                    cloudinary
            );

            // Upload other images
            List<String> imageUrls = new ArrayList<>();
            if (addListingRequest.getImages() != null) {
                for (MultipartFile image : addListingRequest.getImages()) {
                    String imageUrl = uploadImageToCloudinary(
                            image,
                            assetFolderImage,
                            publicId + "_" + UUID.randomUUID().toString(),
                            cloudinary
                    );
                    imageUrls.add(imageUrl);
                }
            }

            // Map DTO to Entity
            Listing listing = AddListingRequestMapper.AddListingMapDTOToEntity(
                    addListingRequest,
                    featuredImageUrl,
                    imageUrls,
                    newId
            );
            listing.setActive(true);
            listing.setSold(false);

            // Save to repository
            Listing savedListing = listingRepository.save(listing);

            // Publish event
            eventPublisher.publishListingCreatedEvent(savedListing);

            // Map Entity to DTO with property information
            GetPropertyResponse propertyResponse = null;
            if (savedListing.getPropertyId() != null) {
                propertyResponse = propertyService.getPropertyById(savedListing.getPropertyId()).orElse(null);
            }
            return GetListingResponseMapper.GetListingMapEntityToDTO(savedListing, propertyResponse);
        } catch (Exception e) {
            log.error("Error creating listing: ", e);
            throw new RuntimeException("Failed to create listing: " + e.getMessage());
        }
    }

    @Override
    public Optional<GetListingResponse> getListingById(String id) {
        log.info("Fetching listing with id: {}", id);
        return listingRepository.findById(id)
                .map(listing -> {
                    GetPropertyResponse property = null;
                    if (listing.getPropertyId() != null) {
                        property = propertyService.getPropertyById(listing.getPropertyId()).orElse(null);
                    }
                    return GetListingResponseMapper.GetListingMapEntityToDTO(listing, property);
                });
    }

    @Override
    public GetListingResponse updateListing(String id, UpdateListingRequest request) {
        log.info("Updating listing with id: {}", id);
        try {
            Listing existingListing = listingRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Listing not found with id: " + id));

            // Update fields with null check
            if (request.getPropertyId() != null && !request.getPropertyId().equals(existingListing.getPropertyId())) {
                existingListing.setPropertyId(request.getPropertyId());
            }

            if (request.getName() != null && !request.getName().equals(existingListing.getName())) {
                existingListing.setName(request.getName());
            }
            
            if (request.getDescription() != null && !request.getDescription().equals(existingListing.getDescription())) {
                existingListing.setDescription(request.getDescription());
            }
            
            if (request.getPrice() > 0 && request.getPrice() != existingListing.getPrice()) {
                existingListing.setPrice(request.getPrice());
            }
            
            if (request.getListingType() != null && !request.getListingType().equals(existingListing.getListingType().toString())) {
                existingListing.setListingType(ListingType.valueOf(request.getListingType().toUpperCase()));
            }
            
            if (request.getAgentId() != null && !request.getAgentId().equals(existingListing.getAgentId())) {
                existingListing.setAgentId(request.getAgentId());
            }
            
            if (request.getBedrooms() > 0 && request.getBedrooms() != existingListing.getBedrooms()) {
                existingListing.setBedrooms(request.getBedrooms());
            }
            
            if (request.getBathrooms() > 0 && request.getBathrooms() != existingListing.getBathrooms()) {
                existingListing.setBathrooms(request.getBathrooms());
            }
            
            if (request.getArea() > 0 && request.getArea() != existingListing.getArea()) {
                existingListing.setArea(request.getArea());
            }
            
            existingListing.setSold(request.isSold());

            // Update featured image
            if (request.getFeaturedImage() != null) {
                String assetFolderImage = CommonCloudinaryAttribute.assetFolderListing;
                String publicId = Listing.class.getSimpleName() + "_" + id;

                // Upload new featured image
                String featuredImageUrl = uploadImageToCloudinary(
                        request.getFeaturedImage(),
                        assetFolderImage,
                        publicId + "_featured",
                        cloudinary
                );
                existingListing.setFeaturedImageUrl(featuredImageUrl);
            }

            // Update other images
            if (request.getImages() != null) {
                List<String> imageUrls = new ArrayList<>();
                for (MultipartFile image : request.getImages()) {
                    String assetFolderImage = CommonCloudinaryAttribute.assetFolderListing;
                    String publicId = Listing.class.getSimpleName() + "_" + id;

                    // Upload new images
                    String imageUrl = uploadImageToCloudinary(
                            image,
                            assetFolderImage,
                            publicId + "_" + UUID.randomUUID(),
                            cloudinary
                    );
                    imageUrls.add(imageUrl);
                }
                existingListing.setImageUrls(imageUrls);
            }
            Listing updatedListing = listingRepository.save(existingListing);
            eventPublisher.publishListingUpdatedEvent(updatedListing);
            
            // Get property information
            GetPropertyResponse propertyResponse = null;
            if (updatedListing.getPropertyId() != null) {
                propertyResponse = propertyService.getPropertyById(updatedListing.getPropertyId()).orElse(null);
            }
            
            return GetListingResponseMapper.GetListingMapEntityToDTO(updatedListing, propertyResponse);
        } catch (Exception e) {
            log.error("Error updating listing: ", e);
            throw new RuntimeException("Failed to update listing: " + e.getMessage());
        }
    }

    @Override
    public boolean deleteListing(String id) {
        log.info("Deleting listing with id: {}", id);
        Optional<Listing> existingListing = listingRepository.findById(id);
        if (existingListing.isEmpty()) {
            return false;
        }
        Listing listingToDelete = existingListing.get();
        listingToDelete.setActive(false);
        listingRepository.save(listingToDelete);
        eventPublisher.publishListingDeletedEvent(listingToDelete);
        return true;
    }

    @Override
    public List<GetListingResponse> findByAgentId(String agentId, GetListingRequest request) {
        log.info("Finding listings by agent id with pagination: {}", agentId);
        
        // Set default values if null or invalid
        int page = (request.getPage() != null && request.getPage() >= 0) ? request.getPage() : 0;
        int size = (request.getSize() != null && request.getSize() > 0) ? request.getSize() : 10;
        String sort = (request.getSort() != null && !request.getSort().isEmpty()) ? request.getSort() : "createdAt";
        String direction = (request.getDirection() != null && !request.getDirection().isEmpty()) ? 
                request.getDirection().toUpperCase() : "DESC";
                log.debug("Pagination: page={}, size={}, sort={}, direction={}", page, size, sort, direction);
    
        // Tìm kiếm danh sách
        int offset = page > 0 ? (page - 1) * size : 0;
        List<Listing> listings = listingRepository.findByAgentId(agentId, size, offset);
        log.info("Found {} listings for agent id: {}", listings.size(), agentId);

        // Chuyển đổi và trả về danh sách với thông tin property
        return listings.stream()
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<GetListingResponse> findByLocation(double latitude, double longitude, double maxDistanceKm) {
        log.info("Finding listings by location: lat={}, lng={}, maxDistance={}km", latitude, longitude, maxDistanceKm);
        
        // Chuyển đổi khoảng cách km sang độ để tìm kiếm
        double kmToDegree = 1 / 111.12;
        double minLat = latitude - (maxDistanceKm * kmToDegree);
        double maxLat = latitude + (maxDistanceKm * kmToDegree);
        double minLon = longitude - (maxDistanceKm * kmToDegree / Math.cos(Math.toRadians(latitude)));
        double maxLon = longitude + (maxDistanceKm * kmToDegree / Math.cos(Math.toRadians(latitude)));
        
        // Trước tiên, tìm tất cả các property trong phạm vi cần tìm
        List<String> propertyIds = propertyService.findPropertyIdsByLocation(minLat, maxLat, minLon, maxLon);
        
        if (propertyIds.isEmpty()) {
            return new ArrayList<>();
        }
        
        // Sau đó, tìm các listing với propertyId nằm trong danh sách đã tìm được
        List<Listing> listings = listingRepository.findByPropertyIds(propertyIds);
        
        // Chuyển đổi và trả về danh sách với thông tin property
        return listings.stream()
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<GetListingResponse> getAllListings(GetListingRequest request) {
        log.info("Searching listings with criteria");
        
        // Set default values if null or invalid
        int page = (request.getPage() != null && request.getPage() >= 0) ? request.getPage() : 0;
        int size = (request.getSize() != null && request.getSize() > 0) ? request.getSize() : 10;
        String sort = (request.getSort() != null && !request.getSort().isEmpty()) ? request.getSort() : "createdAt";
        String direction = (request.getDirection() != null && !request.getDirection().isEmpty()) ? 
                request.getDirection().toUpperCase() : "DESC";
        log.debug("Pagination: page={}, size={}, sort={}, direction={}", page, size, sort, direction);
    
        // Tìm kiếm danh sách
        int offset = page > 0 ? (page - 1) * size : 0;
        
        List<Listing> listings = listingRepository.search(
                request.getListingType(),
                request.getPropertyType(),
                request.getCity(),
                request.getDistrict(),
                request.getMinPrice(),
                request.getMaxPrice(),
                request.getMinBedrooms(),
                request.getMaxBedrooms(),
                request.getMinArea(),
                request.getMaxArea(),
                size,
                offset
        );
        log.info("Found {} listings matching criteria", listings.size());
        
        // Chuyển đổi và trả về danh sách với thông tin property
        return listings.stream()
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public long countSearchResults(GetListingRequest request) {
        return listingRepository.countSearchResults(
                request.getListingType(),
                request.getPropertyType(),
                request.getCity(),
                request.getDistrict(),
                request.getMinPrice(),
                request.getMaxPrice(),
                request.getMinBedrooms(),
                request.getMaxBedrooms(),
                request.getMinArea(),
                request.getMaxArea()
        );
    }
    
    @Override
    public List<GetListingResponse> findByAddressKeyword(String keyword) {
        log.info("Finding listings by address keyword: {}", keyword);
        
        if (keyword == null || keyword.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        // Tìm các properties dựa trên từ khóa địa chỉ
        List<GetPropertyResponse> properties = propertyService.findPropertiesByAddressKeyword(keyword);
        
        if (properties.isEmpty()) {
            return new ArrayList<>();
        }
        
        // Lấy danh sách propertyIds từ các properties tìm được
        List<String> propertyIds = properties.stream()
                .map(GetPropertyResponse::getPropertyId)
                .collect(Collectors.toList());
        
        // Tìm các listings dựa trên propertyIds
        List<Listing> listings = listingRepository.findByPropertyIds(propertyIds);
        
        // Chuyển đổi và trả về listings kèm thông tin property
        return listings.stream()
                .map(listing -> {
                    // Tìm property tương ứng với listing
                    GetPropertyResponse propertyResponse = properties.stream()
                            .filter(p -> p.getPropertyId().equals(listing.getPropertyId()))
                            .findFirst()
                            .orElse(null);
                    
                    return GetListingResponseMapper.GetListingMapEntityToDTO(listing, propertyResponse);
                })
                .collect(Collectors.toList());
    }
    
    // Helper method to map Entity to Response DTO with property information
    private GetListingResponse mapEntityToResponse(Listing listing) {
        if (listing == null) return null;
        
        GetPropertyResponse propertyResponse = null;
        if (listing.getPropertyId() != null) {
            propertyResponse = propertyService.getPropertyById(listing.getPropertyId()).orElse(null);
        }
        
        return GetListingResponseMapper.GetListingMapEntityToDTO(listing, propertyResponse);
    }
    
    private String uploadImageToCloudinary(MultipartFile file, String assetFolder, String publicId, Cloudinary cloudinary) throws Exception {
        // Danh sách các loại file được phép
        List<String> allowedContentTypes = Arrays.asList(
                CommonFileType.JPEG,
                CommonFileType.PNG,
                CommonFileType.WEBP,
                CommonFileType.GIF
        );
        String contentType = file.getContentType();

        // Kiểm tra loại file
        if (!allowedContentTypes.contains(contentType)) {
            throw new IllegalArgumentException("Invalid file type: " + contentType + ". Allowed types: " + String.join(", ", allowedContentTypes));
        }

        // Lưu file tạm thời vào local
        String folderPath = Paths.get(System.getProperty("user.dir"), "upload").toString();
        File uploadDir = new File(folderPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        File tempFile = new File(folderPath, file.getOriginalFilename());
        file.transferTo(tempFile);

        // Upload lên Cloudinary
        Map uploadResult = cloudinary.uploader().upload(tempFile, ObjectUtils.asMap(
                "folder", assetFolder,
                "public_id", publicId
        ));

        // Xóa file tạm thời
        tempFile.delete();

        // Trả về URL của ảnh
        return (String) uploadResult.get("secure_url");
    }

}