// mockService.js
const handleFetchListMock = async () => {
    const dummyData = [
        { id: 1, name: "John Doe" },
        { id: 2, name: "Jane Smith" },
        { id: 3, name: "Alice Johnson" },
    ];

    return dummyData;
};

export { handleFetchListMock };